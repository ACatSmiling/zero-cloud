package cn.zero.cloud.component.cache.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Xisun Wang
 * @since 6/13/2024 17:28
 */
@ConfigurationProperties(prefix = "zero.cloud.cache")
public class ZeloudCacheProperties {

    /**
     * {@link #redisScanBatchSize} 默认值
     */
    private static final Integer REDIS_SCAN_BATCH_SIZE_DEFAULT = 30;

    /**
     * redis scan 一次返回数量
     */
    private Integer redisScanBatchSize = REDIS_SCAN_BATCH_SIZE_DEFAULT;

    public Integer getRedisScanBatchSize() {
        return redisScanBatchSize;
    }

    public void setRedisScanBatchSize(Integer redisScanBatchSize) {
        this.redisScanBatchSize = redisScanBatchSize;
    }
}
