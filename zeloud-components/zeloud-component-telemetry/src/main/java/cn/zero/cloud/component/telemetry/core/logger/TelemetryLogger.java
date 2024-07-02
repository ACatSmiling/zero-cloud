package cn.zero.cloud.component.telemetry.core.logger;

import cn.zero.cloud.component.telemetry.core.pojo.TelemetryLog;

/**
 * @author Xisun Wang
 * @since 2024/3/21 12:23
 */
public interface TelemetryLogger {
    void info(TelemetryLog telemetryLog);
}
