package cn.zero.cloud.business.controller;

import cn.zero.cloud.business.common.constants.CustomizeStatusConstants;
import cn.zero.cloud.component.exception.type.impl.PlatFormJsonException;
import cn.zero.cloud.component.exception.type.impl.RestResponseException;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * @author Xisun Wang
 * @since 2024/3/26 17:19
 */
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
    @GetMapping(value = "/normal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String normalException(@RequestParam(name = "code", required = false, defaultValue = "2") int code) throws Exception {
        if (code == 1) {
            throw new Exception("it is a test exception");
        }
        return "hello, world!";
    }

    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String restException(@RequestParam(name = "code", required = false, defaultValue = "2") int code) {
        if (code == 1) {
            // RestResponseException exception = new RestResponseException(HttpStatus.CONFLICT.value(), "1122");
            RestResponseException exception = new RestResponseException(CustomizeStatusConstants.ABC.value(), "1122");
            exception.addAdditionalMessage("bbcc");
            exception.addAdditionalMessage("eeff");
            exception.addAdditionalMessage("9988");
            exception.addAdditionalMessage("7766");
            throw exception;
        }
        return "hello, world!";
    }

    @GetMapping(value = "/feign", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String feignException(@RequestParam(name = "code", required = false, defaultValue = "2") int code) {
        if (code == 1) {
            throw FeignException.errorStatus(
                    "ExampleFeignException",
                    Response.builder()
                            .status(503)
                            .reason("Bad Request")
                            .request(Request.create(
                                    Request.HttpMethod.GET,
                                    "https://example.com",
                                    Collections.emptyMap(),
                                    null,
                                    null,
                                    null))
                            .build());
        }
        return "hello, world!";
    }

    @GetMapping(value = "/illegal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String illegalException(@RequestParam(name = "code", required = false, defaultValue = "2") int code) {
        if (code == 1) {
            throw new IllegalStateException("illegal state!");
        }
        return "hello, world!";
    }

    @GetMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String platFormJsonException(@RequestParam(name = "code", required = false, defaultValue = "2") int code) {
        if (code == 1) {
            // PlatFormJsonException exception = new PlatFormJsonException(HttpStatus.NO_CONTENT.value(), "adsad");
            PlatFormJsonException exception = new PlatFormJsonException(CustomizeStatusConstants.ABC.value(), "adsad");
            exception.addAdditionalMessage("json1");
            exception.addAdditionalMessage("json2");
            exception.addAdditionalMessage("json3");
            throw exception;
        }
        return "hello, world!";
    }
}
