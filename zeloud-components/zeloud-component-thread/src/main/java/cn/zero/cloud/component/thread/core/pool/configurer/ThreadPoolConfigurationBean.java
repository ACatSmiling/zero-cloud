package cn.zero.cloud.component.thread.core.pool.configurer;

import cn.zero.cloud.component.thread.core.local.transmitter.ThreadPoolThreadLocalTransmitter;
import cn.zero.cloud.component.thread.core.telemetry.ThreadPoolDecoratorTelemetryLog;

import java.util.List;

public class ThreadPoolConfigurationBean {
    /**
     * 线程池ThreadLocal
     */
    private List<ThreadPoolThreadLocalTransmitter> transmitters;

    /**
     * 线程池日志装饰器
     */
    private ThreadPoolDecoratorTelemetryLog decorator;

    public ThreadPoolConfigurationBean(List<ThreadPoolThreadLocalTransmitter> transmitters, ThreadPoolDecoratorTelemetryLog decorator) {
        this.transmitters = transmitters;
        this.decorator = decorator;
    }

    public List<ThreadPoolThreadLocalTransmitter> getTransmitters() {
        return transmitters;
    }

    public void setTransmitters(List<ThreadPoolThreadLocalTransmitter> transmitters) {
        this.transmitters = transmitters;
    }

    public ThreadPoolDecoratorTelemetryLog getDecorator() {
        return decorator;
    }

    public void setDecorator(ThreadPoolDecoratorTelemetryLog decorator) {
        this.decorator = decorator;
    }
}
