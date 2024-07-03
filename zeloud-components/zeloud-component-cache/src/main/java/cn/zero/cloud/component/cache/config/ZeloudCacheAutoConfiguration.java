package cn.zero.cloud.component.cache.config;

import cn.hutool.core.util.StrUtil;
import cn.zero.cloud.component.cache.config.properties.ZeloudCacheProperties;
import cn.zero.cloud.component.cache.core.CustomTtlRedisCacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.zero.cloud.component.cache.config.ZeloudRedisAutoConfiguration.buildRedisSerializer;

/**
 * @author Xisun Wang
 * @since 6/14/2024 09:13
 */
@AutoConfiguration
@EnableConfigurationProperties({CacheProperties.class, ZeloudCacheProperties.class})
@EnableCaching // 开启缓存支持
public class ZeloudCacheAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZeloudCacheAutoConfiguration.class);

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000));
        return cacheManager;
    }

    /**
     * RedisCacheConfiguration Bean
     * <p>
     * 参考 org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration 的 createConfiguration 方法
     */
    @Bean
    @Primary
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        // 定义缓存配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        // 设置缓存键使用 String 序列化方式
        config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));

        // 设置缓存值使用 JSON 序列化方式
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(buildRedisSerializer()));

        // 设置缓存键的前缀生成策略
        // 设置使用 : 单冒号，而不是双 :: 冒号，避免 Redis Desktop Manager 多余空格
        // 详细可见 https://blog.csdn.net/chuixue24/article/details/103928965 博客
        // 再次修复单冒号，而不是双 :: 冒号问题，Issues 详情：https://gitee.com/zhijiantianya/yudao-cloud/issues/I86VY2
        config = config.computePrefixWith(cacheName -> {
            String keyPrefix = cacheProperties.getRedis().getKeyPrefix();
            if (StringUtils.hasText(keyPrefix)) {
                keyPrefix = keyPrefix.lastIndexOf(StrUtil.COLON) == -1 ? keyPrefix + StrUtil.COLON : keyPrefix;
                return keyPrefix + cacheName + StrUtil.COLON;
            }
            return cacheName + StrUtil.COLON;
        });

        // 设置 CacheProperties.Redis 的属性
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            // 如果 getTimeToLive() 返回不为 null，则设置缓存有效期，默认为 null
            config = config.entryTtl(Duration.ofHours(redisProperties.getTimeToLive().toHours()));
        }
        if (!redisProperties.isCacheNullValues()) {
            // 如果 isCacheNullValues() 返回 false，则设置不缓存空值，默认为 true
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            // 如果 isUseKeyPrefix() 返回 false，则设置禁用缓存前缀，默认为 true
            config = config.disableKeyPrefix();
        }
        return config;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate,
                                               RedisCacheConfiguration redisCacheConfiguration,
                                               ZeloudCacheProperties zeloudCacheProperties) {
        // 创建 RedisCacheWriter 对象
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());

        // RedisCacheWriter 是一个底层的类，用于定义缓存的写入操作
        // nonLockingRedisCacheWriter 实例，写缓存操作时不使用锁，可以提高性能，但在某些场景下可能会牺牲一些一致性
        // BatchStrategies.scan() 指定的批量大小来执行扫描操作
        RedisCacheWriter cacheWriter = RedisCacheWriter
                .nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.scan(zeloudCacheProperties.getRedisScanBatchSize()));

        // 创建 TenantRedisCacheManager 对象
        return new CustomTtlRedisCacheManager(cacheWriter, redisCacheConfiguration);
    }

    @Bean
    @Primary // 存在多个同类型的 Bean 时，应该优先考虑当前 Bean
    public CacheManager cacheManager(CacheManager caffeineCacheManager,
                                     CacheManager redisCacheManager,
                                     CacheProperties cacheProperties) {
        // 根据配置，确定优先使用的 CacheManager
        // 如果想在特定的缓存操作中使用非默认的缓存管理器，可以在 @Cacheable、@CachePut、@CacheEvict 等注解中指定 cacheManager 属性
        CacheType cacheType = cacheProperties.getType();
        return switch (cacheType) {
            case CAFFEINE -> caffeineCacheManager;
            case REDIS -> redisCacheManager;
            default -> throw new IllegalArgumentException("cache type error!");
        };
    }
}
