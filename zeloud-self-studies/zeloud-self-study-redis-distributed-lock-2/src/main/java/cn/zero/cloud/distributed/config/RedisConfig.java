package cn.zero.cloud.distributed.config;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Xisun Wang
 * @since 2024/6/14 23:20
 */
@Configuration
public class RedisConfig {
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
}
