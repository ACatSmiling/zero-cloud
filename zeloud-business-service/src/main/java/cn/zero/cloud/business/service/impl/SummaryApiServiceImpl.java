package cn.zero.cloud.business.service.impl;

import cn.zero.cloud.component.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.component.kafka.producer.business.event.impl.SummaryGeneratedEvent;
import cn.zero.cloud.business.service.SummaryApiService;
import cn.zero.cloud.component.general.tool.utils.ZeloudJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Xisun Wang
 * @since 2024/4/12 16:21
 */
@Slf4j
@Service
public class SummaryApiServiceImpl implements SummaryApiService {
    private ApplicationContext context;

    @Autowired
    public SummaryApiServiceImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void sendSummaryMessage() {
        SummaryMessage message = new SummaryMessage();
        message.setSummaryUUID(UUID.randomUUID().toString());
        SummaryGeneratedEvent summaryGeneratedEvent = new SummaryGeneratedEvent(this, message);
        log.info("message: {}", ZeloudJsonUtil.serializeToJson(message));
        context.publishEvent(summaryGeneratedEvent);
    }
}
