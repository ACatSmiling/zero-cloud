package cn.zero.cloud.component.telemetry.core.decorator.impl;

import cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.core.decorator.TelemetryDecorator;
import cn.zero.cloud.component.telemetry.core.factory.TelemetryDecoratorFactory;
import cn.zero.cloud.component.telemetry.core.filter.impl.ExampleFeatureNameFilter;
import cn.zero.cloud.component.telemetry.core.logger.AbstractTelemetryLogger;
import cn.zero.cloud.component.telemetry.core.pojo.TelemetryLog;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * 特殊日志处理装饰器接口实现类，示例
 *
 * @author Xisun Wang
 * @since 2024/3/20 15:52
 */
@Service
public class ExampleObjectTypeTelemetryDecorator implements TelemetryDecorator {
    @PostConstruct
    private void postConstruct() {
        // 注册当前 Telemetry Decorator
        TelemetryDecoratorFactory.register(TelemetryConstants.OBJECT_TYPE_EXAMPLE, this);
        // 设置 featureName 过滤器
        AbstractTelemetryLogger.setFilter(new ExampleFeatureNameFilter());
    }

    @Override
    public void buildTelemetryItems(Object result, TelemetryLog telemetryLog) {
        // TODO 针对 result，当前 decorator 需进行的特定处理，如果没有，则忽略此 decorator 的注册

        // 切面日志的通用处理
        setCommonMessage(telemetryLog);
    }

    @Override
    public void setCommonMessage(TelemetryLog telemetryLog) {
        // TODO 根据业务，选择是否重写 setCommonMessage 方法
    }
}
