package cn.zero.cloud.component.kafka.producer.business.event.impl;

import cn.zero.cloud.component.kafka.common.constants.OperationType;
import cn.zero.cloud.component.kafka.common.message.internal.transcript.TranscriptMessage;
import cn.zero.cloud.component.kafka.producer.business.event.AbstractTranscriptChangedEvent;

public class TranscriptDeleteEvent extends AbstractTranscriptChangedEvent {
    public TranscriptDeleteEvent(Object source, TranscriptMessage transcriptMessage) {
        super(source, transcriptMessage);
        transcriptMessage.setOperationType(OperationType.DELETE);
    }
}
