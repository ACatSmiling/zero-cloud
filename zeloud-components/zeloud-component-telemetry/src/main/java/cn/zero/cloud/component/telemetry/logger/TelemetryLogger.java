package cn.zero.cloud.component.telemetry.logger;

import cn.zero.cloud.component.telemetry.pojo.TelemetryLog;

/**
 * @author Xisun Wang
 * @since 2024/3/21 12:23
 */
public interface TelemetryLogger {
    void info(TelemetryLog telemetryLog);
}
