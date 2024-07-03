package cn.zero.cloud.business.service;

import org.springframework.scheduling.annotation.Async;

import static cn.zero.cloud.component.async.config.ZeloudAsyncTaskAutoConfiguration.NOTIFY_THREAD_POOL_TASK_EXECUTOR;

/**
 * @author Xisun Wang
 * @since 7/3/2024 15:29
 */
public interface AsyncTaskService {
    @Async(value = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    void asyncTaskSuccess();

    @Async(value = NOTIFY_THREAD_POOL_TASK_EXECUTOR)
    void asyncTaskFailed();
}
