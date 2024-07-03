package cn.zero.cloud.business.controller;

import cn.zero.cloud.business.service.AsyncTaskService;
import cn.zero.cloud.component.telemetry.core.Telemetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static cn.zero.cloud.business.common.telemetry.BusinessTelemetryConstants.*;
import static cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants.VERB_GET;

/**
 * @author Xisun Wang
 * @since 7/3/2024 14:00
 */
@Slf4j
@RestController
@RequestMapping(value = "/async/task")
public class AsyncTaskController {
    private final AsyncTaskService asyncTaskService;

    @Autowired
    public AsyncTaskController(AsyncTaskService asyncTaskService) {
        this.asyncTaskService = asyncTaskService;
    }

    @Telemetry(moduleName = BUSINESS_MODULE, metricName = COMPONENT_ASYNC_INTEGRATION, featureName = COMPONENT_ASYNC, verb = VERB_GET, objectType = COMPONENT_ASYNC_OBJECT)
    @GetMapping(value = "/success", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void asyncSuccess() {
        asyncTaskService.asyncTaskSuccess();
        log.info("this is main thread for method asyncSuccess");
    }

    @Telemetry(moduleName = BUSINESS_MODULE, metricName = COMPONENT_ASYNC_INTEGRATION, featureName = COMPONENT_ASYNC, verb = VERB_GET, objectType = COMPONENT_ASYNC_OBJECT)
    @GetMapping(value = "/failed", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void asyncFailed() {
        asyncTaskService.asyncTaskFailed();
        log.info("this is main thread for method asyncFailed");
    }
}
