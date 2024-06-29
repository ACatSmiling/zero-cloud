package cn.zero.cloud.component.kafka.common.telemetry.factory;

import cn.zero.cloud.component.kafka.common.pojo.context.ConsumerContext;
import cn.zero.cloud.component.kafka.common.telemetry.pojo.KafkaConsumerTelemetryLog;
import cn.zero.cloud.component.kafka.utils.KafkaCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:21
 */
public class KafkaConsumerTelemetryLogFactory {
    private KafkaConsumerTelemetryLogFactory() {
        throw new IllegalStateException("Utility class!");
    }

    /**
     * 创建KafkaConsumerTelemetryLog
     *
     * @param record record
     * @return KafkaConsumerTelemetryLog
     */
    public static KafkaConsumerTelemetryLog createLog(ConsumerRecord<?, ?> record) {
        String resourceType = KafkaCommonUtil.getResourceTypeFromTopic(record.topic());
        String featureType = StringUtils.capitalize(resourceType) + "Notification";
        return createLog(featureType, record);
    }

    /**
     * 创建KafkaConsumerTelemetryLog
     *
     * @param featureType 特性类型
     * @param record      record
     * @return KafkaConsumerTelemetryLog
     */
    public static KafkaConsumerTelemetryLog createLog(String featureType, ConsumerRecord<?, ?> record) {
        KafkaConsumerTelemetryLog log = new KafkaConsumerTelemetryLog(featureType);
        log.setTopicName(record.topic());
        log.setPartition(record.partition());
        log.setOffset(record.offset());
        return log;
    }

    /**
     * 创建KafkaConsumerTelemetryLog
     *
     * @param featureType     特性类型
     * @param consumerContext 消费者上下文
     * @return KafkaConsumerTelemetryLog
     */
    public static KafkaConsumerTelemetryLog createLog(String featureType, ConsumerContext consumerContext) {
        KafkaConsumerTelemetryLog log = new KafkaConsumerTelemetryLog(featureType);
        log.setTopicName(consumerContext.getTopic());
        log.setPartition(consumerContext.getPartition());
        log.setOffset(consumerContext.getOffset());
        return log;
    }
}
