package cn.zero.cloud.platform.factory;

import cn.zero.cloud.platform.logger.AbstractTelemetryLogger;
import cn.zero.cloud.platform.filter.FeatureNameFilter;
import cn.zero.cloud.platform.logger.TelemetryLogger;
import cn.zero.cloud.platform.logger.impl.TelemetryLoggerImpl;
import cn.zero.cloud.platform.logger.impl.TelemetryNonQueueLoggerImpl;

/**
 * @author Xisun Wang
 * @since 2024/3/21 12:11
 */
public class TelemetryLoggerFactory {
    private TelemetryLoggerFactory() {
        throw new IllegalStateException("Utility class!");
    }

    public static TelemetryLogger getTelemetryLogger() {
        return TelemetryLoggerImpl.getTelemetryLoggerInstance();
    }

    public static TelemetryLogger getTelemetryLogger(boolean enableInternalQueue) {
        return enableInternalQueue ? TelemetryLoggerImpl.getTelemetryLoggerInstance() :
                TelemetryNonQueueLoggerImpl.getTelemetryLoggerNonQueueInstance();
    }

    public static void initFilter(FeatureNameFilter filter) {
        AbstractTelemetryLogger.setFilter(filter);
    }
}
