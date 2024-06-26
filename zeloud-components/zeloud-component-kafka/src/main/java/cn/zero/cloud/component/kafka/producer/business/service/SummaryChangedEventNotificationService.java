package cn.zero.cloud.component.kafka.producer.business.service;

import cn.zero.cloud.component.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.component.kafka.common.pojo.notification.KafkaNotificationTopics;
import cn.zero.cloud.component.kafka.producer.notification.KafkaNotificationSender;
import cn.zero.cloud.component.general.tool.utils.ZeloudJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SummaryNotificationService，Summary changed event通知处理业务
 */
@Slf4j
@Component
public class SummaryChangedEventNotificationService {
    private final KafkaNotificationTopics kafkaNotificationTopics;

    private final KafkaNotificationSender kafkaNotificationSender;

    public SummaryChangedEventNotificationService(@Autowired KafkaNotificationTopics kafkaNotificationTopics,
                                                  @Autowired KafkaNotificationSender kafkaNotificationSender) {
        this.kafkaNotificationTopics = kafkaNotificationTopics;
        this.kafkaNotificationSender = kafkaNotificationSender;
    }

    public void send(SummaryMessage summaryMessage) {
        try {
            String topic = kafkaNotificationTopics.getSummaryTopic();
            log.info("Send summary changed message, summaryMessage: {}, topic: {}",
                    ZeloudJsonUtil.serializeToJson(summaryMessage), topic);
            kafkaNotificationSender.sendMessage(summaryMessage, topic);
        } catch (Exception e) {
            log.error("Send summary changed message fail, messageUUID: {}, exception: ", summaryMessage.getMessageUUID(), e);
        }
    }
}
