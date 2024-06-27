package cn.zero.cloud.platform.factory;

import cn.zero.cloud.platform.constants.TelemetryConstants;
import cn.zero.cloud.platform.logger.manual.TelemetryCommonTypeLoggerImpl;

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
