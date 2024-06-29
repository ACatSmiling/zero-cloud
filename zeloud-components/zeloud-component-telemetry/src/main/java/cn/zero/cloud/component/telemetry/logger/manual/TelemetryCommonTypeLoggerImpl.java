package cn.zero.cloud.component.telemetry.logger.manual;

import cn.zero.cloud.component.telemetry.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.pojo.TelemetryLog;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * 手动记录日志
 *
 * @author Xisun Wang
 * @since 2024/3/25 11:08
 */
public class TelemetryCommonTypeLoggerImpl extends TelemetryLog {
    private Stopwatch stopwatch;

    public TelemetryCommonTypeLoggerImpl(TelemetryConstants.ObjectType objectType, String ObjectID) {
        this.setModuleType(TelemetryConstants.ModuleType.COMMON_API.getName());
        this.setMetricType(TelemetryConstants.MetricType.COMMON_METRIC.getName());
        this.setObjectType(objectType.getName());
        this.setObjectID(ObjectID);
        this.setFeatureType(TelemetryConstants.FeatureType.COMMON_FEATURE.getName());
    }

    public TelemetryCommonTypeLoggerImpl forCreate() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(TelemetryConstants.VerbType.CREATE.getName());
        return this;
    }

    public TelemetryCommonTypeLoggerImpl forUpdate() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(TelemetryConstants.VerbType.UPDATE.getName());
        return this;
    }

    public TelemetryCommonTypeLoggerImpl forDelete() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(TelemetryConstants.VerbType.DELETE.getName());
        return this;
    }

    public TelemetryCommonTypeLoggerImpl forSelect() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(TelemetryConstants.VerbType.SELECT.getName());
        return this;
    }

    public void createSuccess() {
        this.setProcessTime(this.stopwatch.elapsed(TimeUnit.MILLISECONDS));
        this.logForSuccess();
        this.stopwatch.stop();
    }

    public void createFailure(String msg) {
        this.setProcessTime(this.stopwatch.elapsed(TimeUnit.MILLISECONDS));
        this.logForFailure(msg);
        this.stopwatch.stop();
    }
}
