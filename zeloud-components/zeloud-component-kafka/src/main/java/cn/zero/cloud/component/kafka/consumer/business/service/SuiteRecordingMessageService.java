package cn.zero.cloud.component.kafka.consumer.business.service;

import cn.zero.cloud.component.kafka.common.message.internal.recording.SuiteRecordingMessage;
import cn.zero.cloud.component.kafka.common.pojo.result.impl.ConsumeResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * message业务消费
 *
 * @author Xisun Wang
 * @since 2024/3/8 17:30
 */
@Slf4j
@Service
public class SuiteRecordingMessageService {
    public ConsumeResult consume(SuiteRecordingMessage message) {
        ConsumeResult result = ConsumeResult.createConsumeResult(message);
        try {
            // 业务处理成功
            log.info("Consume suite recording success! TrackingID: {}", message.getTrackingID());
            result.generateSuccessResult();
        } catch (Exception e) {
            // 业务处理失败
            log.error("Consume suite recording failed! TrackingID: {}", message.getTrackingID(), e);
            result.generateFailureResult("Consume suite recording failed " + e.getMessage());
        }
        return result;
    }
}
