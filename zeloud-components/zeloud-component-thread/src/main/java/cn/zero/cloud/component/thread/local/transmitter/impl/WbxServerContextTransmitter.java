package cn.zero.cloud.component.thread.local.transmitter.impl;

import cn.zero.cloud.component.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;
import cn.zero.cloud.component.thread.local.context.WbxServerContext;
import cn.zero.cloud.component.thread.local.context.WbxServerContextToken;

public class WbxServerContextTransmitter implements ThreadPoolThreadLocalTransmitter<WbxServerContextToken> {
    @Override
    public WbxServerContextToken get() {
        return WbxServerContext.getContext();
    }

    @Override
    public void set(WbxServerContextToken context) {
        WbxServerContext.setContext(context);
    }

    @Override
    public void clear() {
        WbxServerContext.clearContext();
    }
}
