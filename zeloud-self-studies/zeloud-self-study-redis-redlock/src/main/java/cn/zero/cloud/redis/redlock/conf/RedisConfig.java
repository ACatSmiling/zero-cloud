package cn.zero.cloud.redis.redlock.conf;

import cn.hutool.core.util.ReflectUtil;
import cn.zero.cloud.redis.redlock.conf.properties.RedlockProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Xisun Wang
 * @since 2024/7/1 22:08
 */
@Configuration
public class RedisConfig {
    private final RedlockProperties redlockProperties;

    @Autowired
    public RedisConfig(RedlockProperties redlockProperties) {
        this.redlockProperties = redlockProperties;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用String序列化方式，序列化key
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // 使用JSON序列化方式(使用的是Jackson库)，序列化value
        redisTemplate.setValueSerializer(buildRedisSerializer());
        redisTemplate.setHashValueSerializer(buildRedisSerializer());

        // 初始化RedisTemplate序列化设置
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public static RedisSerializer<?> buildRedisSerializer() {
        RedisSerializer<Object> json = RedisSerializer.json();

        // 解决LocalDateTime的序列化
        ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(json, "mapper");
        objectMapper.registerModules(new JavaTimeModule());
        return json;
    }

    @Bean
    public RedissonClient redissonClient1() {
        Config config = new Config();
        String node = redlockProperties.getSingleAddress1();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        config.useSingleServer()
                .setAddress(node)
                .setDatabase(1)
                .setPassword("123456");
        return Redisson.create(config);
    }

    @Bean
    RedissonClient redissonClient2() {
        Config config = new Config();
        String node = redlockProperties.getSingleAddress2();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        config.useSingleServer()
                .setAddress(node)
                .setDatabase(1)
                .setPassword("123456");
        return Redisson.create(config);
    }

    @Bean
    RedissonClient redissonClient3() {
        Config config = new Config();
        String node = redlockProperties.getSingleAddress3();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        config.useSingleServer()
                .setAddress(node)
                .setDatabase(1)
                .setPassword("123456");
        return Redisson.create(config);
    }
}
