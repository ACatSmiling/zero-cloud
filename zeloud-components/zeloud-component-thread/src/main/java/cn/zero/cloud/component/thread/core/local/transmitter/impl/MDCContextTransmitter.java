package cn.zero.cloud.component.thread.core.local.transmitter.impl;

import cn.zero.cloud.component.thread.core.local.transmitter.ThreadPoolThreadLocalTransmitter;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class MDCContextTransmitter implements ThreadPoolThreadLocalTransmitter<Map<String, String>> {
    @Override
    public Map<String, String> get() {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return copyOfContextMap == null ? new HashMap<>() : copyOfContextMap;
    }

    @Override
    public void set(Map<String, String> context) {
        MDC.setContextMap(context);
    }

    @Override
    public void clear() {
        MDC.clear();
    }
}