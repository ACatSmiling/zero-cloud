package cn.zero.cloud.component.kafka.producer.business.event;

import cn.zero.cloud.component.kafka.common.message.internal.transcript.TranscriptMessage;
import org.springframework.context.ApplicationEvent;

/**
 * Transcript changed事件
 */
public abstract class AbstractTranscriptChangedEvent extends ApplicationEvent {
    protected TranscriptMessage transcriptMessage;

    protected AbstractTranscriptChangedEvent(Object source, TranscriptMessage transcriptMessage) {
        super(source);
        this.transcriptMessage = transcriptMessage;
    }

    public TranscriptMessage getTranscriptMessage() {
        return transcriptMessage;
    }
}
