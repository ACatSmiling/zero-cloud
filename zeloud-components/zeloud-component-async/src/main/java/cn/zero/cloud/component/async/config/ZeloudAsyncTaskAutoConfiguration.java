package cn.zero.cloud.component.async.config;

import com.alibaba.ttl.TtlRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Xisun Wang
 * @since 7/3/2024 11:09
 */
@AutoConfiguration
@EnableConfigurationProperties({TaskExecutionProperties.class})
@EnableAsync // 启用 Spring 的异步方法执行能力，@Async 注解标注的方法，Spring 会启动异步线程来执行
public class ZeloudAsyncTaskAutoConfiguration implements AsyncConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZeloudAsyncTaskAutoConfiguration.class);

    public static final String NOTIFY_THREAD_POOL_TASK_EXECUTOR = "NOTIFY_THREAD_POOL_TASK_EXECUTOR";

    @Bean
    public BeanPostProcessor threadPoolTaskExecutorBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof ThreadPoolTaskExecutor executor)) {
                    return bean;
                }
                // 修改提交的任务，接入 TransmittableThreadLocal，解决在使用线程池时，线程局部变量 ThreadLocal 可能出现的问题
                // 当使用线程池时，由于线程的复用，ThreadLocal 变量可能会被不同的任务共享，这会导致数据混乱
                // TtlRunnable 是 TTL 提供的一种包装器，它可以在执行任务前后正确地传递 ThreadLocal 变量，确保每个任务都能获取到正确的 ThreadLocal 数据，即使是在线程池环境下
                executor.setTaskDecorator(TtlRunnable::get);
                return executor;
            }
        };
    }

    @Bean(NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor notifyThreadPoolTaskExecutor(TaskExecutionProperties taskExecutionProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数，默认为 8
        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
        // 设置最大线程数，默认为 Integer.MAX_VALUE
        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
        // 设置非核心线程的空闲存活时间，默认为 60 秒
        executor.setKeepAliveSeconds((int) taskExecutionProperties.getPool().getKeepAlive().toSeconds());
        // 设置队列大小
        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
        // 设置线程的前缀
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 进行加载
        executor.initialize();
        return executor;
    }

    /**
     * 1. 当异步方法执行抛出异常时，由于它不在主线程中，所以不能直接通过常规的 try-catch 块来捕获
     * 2. getAsyncUncaughtExceptionHandler() 方法允许自定义如何处理这些未捕获的异常
     * 3. 当异步方法抛出异常时，Spring 将调用配置的 AsyncUncaughtExceptionHandler 的 handleUncaughtException() 方法来处理这些异常
     * 4. 默认情况下，如果没有提供自定义的 AsyncUncaughtExceptionHandler，Spring 将使用一个默认的处理器，它仅仅会打印异常堆栈到标准错误输出
     * 5. 自定义异步异常的处理逻辑（例如，记录到日志、发送通知等），可以通过实现 AsyncConfigurer 接口并覆盖 getAsyncUncaughtExceptionHandler() 方法来提供你自己的处理器
     *
     * @return AsyncUncaughtExceptionHandler
     */
    @Bean
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> LOGGER.error("zeloud async task has some error: {}, {}, {}",
                ex.getMessage(),
                method.getDeclaringClass().getName() + "." + method.getName(),
                Arrays.toString(params));
    }
}
