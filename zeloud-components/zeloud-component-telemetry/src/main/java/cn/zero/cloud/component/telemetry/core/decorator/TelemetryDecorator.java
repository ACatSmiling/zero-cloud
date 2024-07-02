package cn.zero.cloud.component.telemetry.core.decorator;

import cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.core.pojo.TelemetryLog;
import org.slf4j.MDC;

/**
 * 特殊日志处理装饰器接口
 *
 * @author Xisun Wang
 * @since 2024/3/14 17:23
 */
public interface TelemetryDecorator {
    void buildTelemetryItems(Object result, TelemetryLog telemetryLog);

    /**
     * 针对切面日志的通用处理，将切面随机生成的 UUID，写入日志中，无实际意义，子类可以重写此方法
     *
     * @param telemetryLog 切面日志
     */
    default void setCommonMessage(TelemetryLog telemetryLog) {
        telemetryLog.setRandomUUID(MDC.get(TelemetryConstants.UUID_KEY_EXAMPLE));
    }
}
