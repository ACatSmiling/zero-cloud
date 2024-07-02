package cn.zero.cloud.business.controller;

import cn.zero.cloud.business.common.entity.dto.UpdateWorldDTO;
import cn.zero.cloud.component.telemetry.core.factory.TelemetryManualLoggerFactory;
import cn.zero.cloud.component.telemetry.core.logger.manual.TelemetryManualLoggerImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Xisun Wang
 * @since 2024/3/6 12:19
 */
@RestController
@RequestMapping(value = "/world")
public class HelloController {
    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld() {
        return "hello, world!";
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateWorld(@RequestBody UpdateWorldDTO updateWorldDTO) {
        Assert.notNull(updateWorldDTO.getWorldSerialNumber(), "World serial number is required.");
        Assert.notNull(updateWorldDTO.getWorldName(), "World name is required.");
        Assert.notNull(updateWorldDTO.getWorldVision(), "World vision is required.");

        TelemetryManualLoggerImpl telemetryManualLoggerImpl = TelemetryManualLoggerFactory
                .getTelemetryManualLogger("COMMON_OBJECT", "1122", "s")
                .forUpdate();
        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println(1 / 0);
        } catch (Exception e) {
            telemetryManualLoggerImpl.setItem("a", "a");
            telemetryManualLoggerImpl.createFailure(e.getMessage());
        }

        return StringUtils.join(updateWorldDTO.getWorldSerialNumber(), "-", updateWorldDTO.getWorldName(), ": ", updateWorldDTO.getWorldVision());
    }
}
