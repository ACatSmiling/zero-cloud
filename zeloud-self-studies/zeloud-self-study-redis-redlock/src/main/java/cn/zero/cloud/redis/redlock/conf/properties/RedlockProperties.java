package cn.zero.cloud.redis.redlock.conf.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Xisun Wang
 * @since 2024/7/1 22:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "zero.loud.redis.redlock")
@Component
public class RedlockProperties {
    private String singleAddress1;

    private String singleAddress2;

    private String singleAddress3;
}
