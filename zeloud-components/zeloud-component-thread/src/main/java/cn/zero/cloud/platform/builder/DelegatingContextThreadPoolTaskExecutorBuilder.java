package cn.zero.cloud.platform.builder;

import cn.zero.cloud.platform.excutor.DelegatingContextThreadPoolTaskExecutor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class DelegatingContextThreadPoolTaskExecutorBuilder implements BeanPostProcessor {
    public static ThreadPoolTaskExecutor newInstance() {
        return new DelegatingContextThreadPoolTaskExecutor();
    }
}
