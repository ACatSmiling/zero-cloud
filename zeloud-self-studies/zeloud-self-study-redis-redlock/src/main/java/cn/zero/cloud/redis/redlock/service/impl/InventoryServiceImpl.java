package cn.zero.cloud.redis.redlock.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.zero.cloud.redis.redlock.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Xisun Wang
 * @since 2024/6/21 22:22
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    @Value("${server.port}")
    private String port;

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedissonClient redissonClient1;

    private final RedissonClient redissonClient2;

    private final RedissonClient redissonClient3;

    @Autowired
    public InventoryServiceImpl(RedisTemplate<String, Object> redisTemplate,
                                RedissonClient redissonClient1,
                                RedissonClient redissonClient2,
                                RedissonClient redissonClient3) {
        this.redisTemplate = redisTemplate;
        this.redissonClient1 = redissonClient1;
        this.redissonClient2 = redissonClient2;
        this.redissonClient3 = redissonClient3;
    }

    @Override
    public String saleByRedlock() {
        String lockName = "RedisDistributedLock";
        String lockValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();

        RLock lock1 = redissonClient1.getLock(lockName);
        RLock lock2 = redissonClient2.getLock(lockName);
        RLock lock3 = redissonClient3.getLock(lockName);

        RedissonMultiLock redLock = new RedissonMultiLock(lock1, lock2, lock3);

        redLock.lock();
        String retMessage;
        try {
            // 1 查询库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory001");
            // 2 判断库存是否足够
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            // 3 扣减库存
            if (inventoryNumber > 0) {
                redisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余：" + inventoryNumber;
            } else {
                retMessage = "商品卖完了";
            }
        } finally {
            redLock.unlock();
        }
        return retMessage + "\t" + "订单号：" + lockValue + "\t" + "服务端口号：" + port;
    }
}
