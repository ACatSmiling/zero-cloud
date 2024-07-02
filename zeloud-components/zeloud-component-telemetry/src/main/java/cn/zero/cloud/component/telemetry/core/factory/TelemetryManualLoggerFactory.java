package cn.zero.cloud.component.telemetry.core.factory;

import cn.zero.cloud.component.telemetry.core.logger.manual.TelemetryManualLoggerImpl;

/**
 * @author Xisun Wang
 * @since 2024/3/25 11:07
 */
public class TelemetryManualLoggerFactory {
    private TelemetryManualLoggerFactory() {
        throw new IllegalStateException("Utility class!");
    }

    public static TelemetryManualLoggerImpl getTelemetryManualLogger(String moduleName, String metricName, String featureName) {
        return new TelemetryManualLoggerImpl(moduleName, metricName, featureName);
    }
}
