package cn.zero.cloud.component.kafka.producer.business.event;

import cn.zero.cloud.component.kafka.common.message.internal.summary.SummaryTranscriptMessage;
import org.springframework.context.ApplicationEvent;

/**
 * SummaryTranscript changed事件
 */
public class AbstractSummaryTranscriptChangeEvent extends ApplicationEvent {
    protected SummaryTranscriptMessage summaryTranscriptMessage;

    public AbstractSummaryTranscriptChangeEvent(Object source,SummaryTranscriptMessage summaryTranscriptMessage) {
        super(source);
        this.summaryTranscriptMessage=summaryTranscriptMessage;
    }

    public SummaryTranscriptMessage getSummaryTranscriptMessage() {
        return summaryTranscriptMessage;
    }
}
