package cn.zero.cloud.platform.service;

/**
 * @author Xisun Wang
 * @since 2024/6/20 22:46
 */
public interface RedissonApiService {
    void addElementToBloomFilter(Object key);

    void checkElementWithBloomFilter(Object key);
}
