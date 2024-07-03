package cn.zero.cloud.business.service.impl;

import cn.zero.cloud.business.service.AsyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Xisun Wang
 * @since 7/3/2024 15:30
 */
@Slf4j
@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {
    @Override
    public void asyncTaskSuccess() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("this is an asynchronous task that executes successfully.");
    }

    @Override
    public void asyncTaskFailed() {
        log.info("this is an asynchronous task that failed.");
        int i = 1 / 0;
    }
}
