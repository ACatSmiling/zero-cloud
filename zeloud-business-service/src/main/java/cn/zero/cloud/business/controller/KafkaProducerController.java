package cn.zero.cloud.business.controller;

import cn.zero.cloud.business.service.SummaryApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xisun Wang
 * @since 2024/4/12 15:53
 */
@RestController
@RequestMapping(value = "/kafka/producer")
public class KafkaProducerController {
    private final SummaryApiService summaryApiServiceImpl;

    @Autowired
    public KafkaProducerController(SummaryApiService summaryApiServiceImpl) {
        this.summaryApiServiceImpl = summaryApiServiceImpl;

    }

    @GetMapping(value = "/message/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void sendSummaryMessage() {
        summaryApiServiceImpl.sendSummaryMessage();
    }
}
