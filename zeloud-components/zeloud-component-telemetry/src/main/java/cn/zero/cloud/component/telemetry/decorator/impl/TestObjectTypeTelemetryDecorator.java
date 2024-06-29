package cn.zero.cloud.component.telemetry.decorator.impl;

import cn.zero.cloud.component.telemetry.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.decorator.TelemetryDecorator;
import cn.zero.cloud.component.telemetry.factory.TelemetryDecoratorFactory;
import cn.zero.cloud.component.telemetry.filter.impl.GlobalFeatureNameFilter;
import cn.zero.cloud.component.telemetry.logger.AbstractTelemetryLogger;
import cn.zero.cloud.component.telemetry.pojo.TelemetryLog;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * @author Xisun Wang
 * @since 2024/3/20 15:52
 */
@Service
public class TestObjectTypeTelemetryDecorator implements TelemetryDecorator {

    @PostConstruct
    private void postConstruct() {
        TelemetryDecoratorFactory.register(TelemetryConstants.ObjectType.TEST_OBJECT.getName(), this);
        AbstractTelemetryLogger.setFilter(new GlobalFeatureNameFilter());
    }

    @Override
    public void buildTelemetryItems(Object result, TelemetryLog telemetryLog) {
        // TODO 针对result，当前decorator需进行的特定处理，如果没有，则忽略此decorator的注册

        // 切面日志的通用处理
        setCommonMessage(telemetryLog);
    }
}
