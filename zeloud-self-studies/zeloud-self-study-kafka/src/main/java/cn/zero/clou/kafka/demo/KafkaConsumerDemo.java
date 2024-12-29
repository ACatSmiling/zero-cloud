package cn.zero.clou.kafka.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/7/27 10:04
 */
@Slf4j
public class KafkaConsumerDemo {
    public static void main(String[] args) {
        // 配置属性
        Map<String, Object> configMap = getStringObjectMap();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configMap)) {
            // 订阅 Topic
            consumer.subscribe(Collections.singletonList("test"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("key: {}, value: {}", record.key(), record.value());
                }
            }
        }
    }

    private static Map<String, Object> getStringObjectMap() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.20:9092");
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configMap.put("group.id", "test");
        configMap.put("enable.auto.commit", "true");
        return configMap;
    }
}
