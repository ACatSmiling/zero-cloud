package cn.zero.cloud.component.cache.config;

import cn.zero.cloud.component.cache.config.properties.CacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Xisun Wang
 * @since 6/14/2024 09:13
 */
@Configuration
@EnableCaching // 开启缓存支持
public class CacheConfig {
    private final CacheProperties cacheProperties;

    @Autowired
    public CacheConfig(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000));
        return cacheManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        long cacheDuration = cacheProperties.getCacheDuration();
        String cacheName = cacheProperties.getCacheName();

        // 定义缓存配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存有效期
                .entryTtl(Duration.ofHours(cacheDuration))
                // 设置缓存键的序列化机制
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                // 设置缓存值的序列化机制
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisConfig.buildRedisSerializer()))
                // 设置缓存键的前缀生成策略，例如AIBRIDGE_CACHE_REDIS_TEST:abcd
                .computePrefixWith(name -> cacheProperties.getCachePrefix() + name + ":")
                // 不缓存空值
                .disableCachingNullValues();

        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(cacheName);

        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put(cacheName, redisCacheConfiguration.entryTtl(Duration.ofHours(cacheDuration)));

        return RedisCacheManager.builder(connectionFactory)
                // 设置默认的缓存配置
                .cacheDefaults(redisCacheConfiguration)
                // 设置RedisCacheManager具有事务感知能力，意味着缓存操作可以参与到Spring管理的事务中，确保缓存的一致性。如果有一个Spring事务正在进行，缓存的变化会在事务成功提交时一起应用
                .transactionAware()
                // 设置初始的缓存名称集合，让RedisCacheManager预先知道这些缓存的存在，从而在应用启动时创建和配置这些缓存
                .initialCacheNames(cacheNames)
                // 为特定的缓存名称提供特殊的缓存配置，可以为不同的缓存名称设置不同的缓存配置
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    @Bean
    @Primary // 存在多个同类型的Bean时，应该优先考虑当前Bean
    public CacheManager cacheManager(CacheManager caffeineCacheManager, CacheManager redisCacheManager) {
        // 根据配置，确定优先使用的CacheManager
        // 如果想在特定的缓存操作中使用非默认的缓存管理器，可以在@Cacheable、@CachePut、@CacheEvict等注解中指定cacheManager属性
        String cacheType = cacheProperties.getCacheType();
        return switch (cacheType) {
            case "caffeine" -> caffeineCacheManager;
            case "redis" -> redisCacheManager;
            default -> throw new IllegalArgumentException("cache type error!");
        };
    }
}
