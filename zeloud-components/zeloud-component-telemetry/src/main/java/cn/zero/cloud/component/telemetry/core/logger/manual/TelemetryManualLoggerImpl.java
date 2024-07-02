package cn.zero.cloud.component.telemetry.core.logger.manual;

import cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.core.pojo.TelemetryLog;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * 手动记录日志
 *
 * @author Xisun Wang
 * @since 2024/3/25 11:08
 */
public class TelemetryManualLoggerImpl extends TelemetryLog {
    private Stopwatch stopwatch;

    public TelemetryManualLoggerImpl(String moduleName, String metricName, String featureName) {
        this.setModuleName(moduleName);
        this.setMetricName(metricName);
        this.setFeatureName(featureName);
    }

    public TelemetryManualLoggerImpl forCreate() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerb(TelemetryConstants.VERB_CREATE);
        return this;
    }

    public TelemetryManualLoggerImpl forUpdate() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerb(TelemetryConstants.VERB_UPDATE);
        return this;
    }

    public TelemetryManualLoggerImpl forDelete() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerb(TelemetryConstants.VERB_DELETE);
        return this;
    }

    public TelemetryManualLoggerImpl forGet() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerb(TelemetryConstants.VERB_GET);
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
