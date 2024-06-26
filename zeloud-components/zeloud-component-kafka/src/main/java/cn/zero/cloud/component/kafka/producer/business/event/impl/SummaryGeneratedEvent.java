package cn.zero.cloud.component.kafka.producer.business.event.impl;

import cn.zero.cloud.component.kafka.common.constants.OperationType;
import cn.zero.cloud.component.kafka.common.message.internal.summary.SummaryMessage;
import cn.zero.cloud.component.kafka.producer.business.event.AbstractSummaryChangeEvent;

/**
 * Summary生成事件
 */
public class SummaryGeneratedEvent extends AbstractSummaryChangeEvent {
    public SummaryGeneratedEvent(Object source, SummaryMessage summaryMessage) {
        super(source, summaryMessage);
        summaryMessage.setOperationType(OperationType.CREATE);
    }
}
