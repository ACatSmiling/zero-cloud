package cn.zero.cloud.component.telemetry.config;

import cn.zero.cloud.component.telemetry.core.TelemetryAspect;
import cn.zero.cloud.component.telemetry.core.service.TelemetryBuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author Xisun Wang
 * @since 7/2/2024 10:13
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "zero.cloud.telemetry", value = "enable", matchIfMissing = true)
public class ZeloudTelemetryAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZeloudTelemetryAutoConfiguration.class);

    @Bean
    public TelemetryBuildService telemetryBuildService() {
        return new TelemetryBuildService();
    }

    @Bean
    public TelemetryAspect telemetryAspect(TelemetryBuildService telemetryBuildService) {
        return new TelemetryAspect(telemetryBuildService);
    }
}
