package cn.zero.cloud.distributed.lock;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Xisun Wang
 * @since 2024/6/25 22:42
 */
@Slf4j
public class RedisDistributedLock implements Lock {
    private final RedisTemplate<String, Object> redisTemplate;

    private final String lockName;
    private final Object lockValue;
    private long expireTime;

    public RedisDistributedLock(RedisTemplate<String, Object> redisTemplate, String lockName, Object lockValue, long expireTime) {
        this.redisTemplate = redisTemplate;
        this.lockName = lockName;
        this.lockValue = lockValue;
        this.expireTime = expireTime;
    }

    @Override
    public void lock() {
        boolean isLock = tryLock();
        log.info("Trace of redis distributed lock, GetLockResult, lockName: {}, lockValue: {}, expireTime: {}, isLock: {}", lockName, lockValue, expireTime, isLock);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        // Redis分布式锁用不到此方法，无需实现
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("Trace of redis distributed lock, GetLockError, lockName: {}, lockValue: {}, error message: ", lockName, lockValue, e);
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, @Nonnull TimeUnit unit) throws InterruptedException {
        if (time != -1L) {
            this.expireTime = unit.toSeconds(time);
        }

        String script =
                "if redis.call('EXISTS',KEYS[1]) == 0 or redis.call('HEXISTS',KEYS[1],ARGV[1]) == 1 then " +
                        "redis.call('HINCRBY',KEYS[1],ARGV[1],1) " +
                        "redis.call('EXPIRE',KEYS[1],ARGV[2]) " +
                        "return 1 " +
                        "else " +
                        "return 0 " +
                        "end";

        while (Boolean.FALSE.equals(redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), List.of(lockName), lockValue, expireTime))) {
            TimeUnit.MILLISECONDS.sleep(50);
        }

        // 自动续期
        renewExpire();

        return true;
    }

    @Override
    public void unlock() {
        String script = "if redis.call('HEXISTS',KEYS[1],ARGV[1]) == 0 then " +
                "return nil " +
                "elseif redis.call('HINCRBY',KEYS[1],ARGV[1],-1) == 0 then " +
                "return redis.call('del',KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";

        Boolean isUnlock = redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockName), lockValue);
        log.info("Trace of redis distributed lock, DeleteLockResult, lockName: {}, lockValue: {}, expireTime: {}, isUnlock: {}", lockName, lockValue, expireTime, isUnlock);

        if (isUnlock == null) {
            throw new RuntimeException("This lock '" + lockName + "' doesn't EXIST");
        }
    }

    /**
     * 锁的自动续期
     */
    private void renewExpire() {
        String script =
                "if redis.call('HEXISTS',KEYS[1],ARGV[1]) == 1 then " +
                        "return redis.call('EXPIRE',KEYS[1],ARGV[2]) " +
                        "else " +
                        "return 0 " +
                        "end";

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Boolean.TRUE.equals(redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockName), lockValue, expireTime))) {
                    renewExpire();
                }
            }
        }, (this.expireTime * 1000) / 3);
    }

    @Override
    public Condition newCondition() {
        // Redis分布式锁用不到此方法，无需实现
        return null;
    }
}
