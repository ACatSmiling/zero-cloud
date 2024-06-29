package cn.zero.cloud.component.telemetry.factory;

import cn.zero.cloud.component.telemetry.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.logger.manual.TelemetryCommonTypeLoggerImpl;

/**
 * @author Xisun Wang
 * @since 2024/3/25 11:07
 */
public class TelemetryManualLoggerFactory {
    private TelemetryManualLoggerFactory() {
        throw new IllegalStateException("Utility class!");
    }

    public static TelemetryCommonTypeLoggerImpl getTelemetryCommonTypeLogger(TelemetryConstants.ObjectType objectType, String objectID) {
        return new TelemetryCommonTypeLoggerImpl(objectType, objectID);
    }
}
