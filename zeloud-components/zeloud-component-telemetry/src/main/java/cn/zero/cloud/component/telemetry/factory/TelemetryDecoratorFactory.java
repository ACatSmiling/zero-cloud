package cn.zero.cloud.component.telemetry.factory;

import cn.zero.cloud.component.telemetry.decorator.TelemetryDecorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xisun Wang
 * @since 2024/3/14 17:19
 */
public class TelemetryDecoratorFactory {
    private TelemetryDecoratorFactory() {
        throw new IllegalStateException("Utility class!");
    }

    private static final Map<String, TelemetryDecorator> TELEMETRY_DECORATOR_FACTORY = new ConcurrentHashMap<>();

    public static void register(String type, TelemetryDecorator service) {
        TELEMETRY_DECORATOR_FACTORY.put(type, service);
    }

    public static TelemetryDecorator getService(String type) {
        return TELEMETRY_DECORATOR_FACTORY.get(type);
    }
}
