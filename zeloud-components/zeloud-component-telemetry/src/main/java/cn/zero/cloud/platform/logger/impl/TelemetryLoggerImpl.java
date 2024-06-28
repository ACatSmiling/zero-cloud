package cn.zero.cloud.platform.logger.impl;

import cn.zero.cloud.platform.logger.AbstractTelemetryLogger;
import cn.zero.cloud.platform.logger.TelemetryLogger;
import cn.zero.cloud.platform.pojo.AsyncLogEvent;
import cn.zero.cloud.platform.handler.AsyncLogEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xisun Wang
 * @since 2024/3/21 12:24
 */
public class TelemetryLoggerImpl extends AbstractTelemetryLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryLoggerImpl.class);

    private static final TelemetryLogger TELEMETRY_LOGGER_INSTANCE = new TelemetryLoggerImpl();

    private static AsyncLogEventHandler logEventHandler;

    private TelemetryLoggerImpl() {
    }

    public static TelemetryLogger getTelemetryLoggerInstance() {
        logEventHandler = AsyncLogEventHandler.getAsyncLogEventHandlerInstance();
        return TELEMETRY_LOGGER_INSTANCE;
    }

    public void writeLog(String logAsJson) {
        AsyncLogEvent logEvent = new AsyncLogEvent(logAsJson, LOGGER);
        logEventHandler.handle(logEvent);
    }
}
