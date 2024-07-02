package cn.zero.cloud.component.thread.core.builder;

import cn.zero.cloud.component.thread.core.excutor.DelegatingContextThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class DelegatingContextThreadPoolTaskExecutorBuilder {
    public static ThreadPoolTaskExecutor newInstance() {
        return new DelegatingContextThreadPoolTaskExecutor();
    }
}
