package cn.zero.cloud.component.async.core;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.*;

/**
 * @author Xisun Wang
 * @since 7/3/2024 09:58
 */
public class FlexibleThreadPool {
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 50;
    private static final long KEEP_ALIVE_MINUTES = 3L;
    private static final int MAX_TASK_SIZE = 1000;
    private static final double THREAD_INCREASE_FACTOR = 1.5;

    // 可动态调整 coreSize 大小的线程池
    private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_MINUTES,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(MAX_TASK_SIZE),
            ThreadUtil.newNamedThreadFactory("zeloud-flexible-thread-pool-", false),
            new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        THREAD_POOL.allowCoreThreadTimeOut(true);

        // 开启定期更新 threadPool 的 coreSize 任务
        schedule();
    }

    private static void schedule() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "AdjustFlexibleThreadPoolCoreSize");
            thread.setDaemon(true);
            return thread;
        });
        // 每 3 秒检测是否需要调整 corePoolSize
        final long period = 3L;
        scheduler.scheduleAtFixedRate(FlexibleThreadPool::adjustThreadPoolCoreSize, 1, period, TimeUnit.SECONDS);
    }

    private static void adjustThreadPoolCoreSize() {
        final int minThreads = CORE_POOL_SIZE;
        final int maxThreads = MAX_POOL_SIZE;
        final double factor = THREAD_INCREASE_FACTOR;

        final int coreSize = THREAD_POOL.getCorePoolSize();
        int size = coreSize;
        final int n = (int) (coreSize / factor);

        if (!THREAD_POOL.getQueue().isEmpty()) {
            // 任务队列中有排队任务，应适当增加 coreSize
            size = (int) (coreSize * factor);
        } else if (THREAD_POOL.getActiveCount() <= n) {
            // 线程池中活跃线程数低于 coreSize/factor 时，应适当减少 coreSize
            size = n + 3;
        } else {
            return;
        }
        // coreSize 不能低于 minThreads，也不能高于 maxThreads
        size = Math.min(Math.max(minThreads, size), maxThreads);

        // 这个判断是必须的 (coreSize 不需要改变时不要调用 setCorePoolSize，否则会频繁 interrupt 因 poll 而阻塞的 thread)
        if (size != coreSize) {
            THREAD_POOL.setCorePoolSize(size);
        }
    }

    public static ThreadPoolExecutor getThreadPool() {
        return THREAD_POOL;
    }

    public static <T> Future<T> submitTask(Callable<T> task) {
        return THREAD_POOL.submit(task);
    }

    public static void submitTask(Runnable task) {
        THREAD_POOL.submit(task);
    }
}
