package cn.zero.clou.kafka.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/7/26 23:44
 */
public class KafkaProducerDemo {
    public static void main(String[] args) {
        // 配置属性
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.20:9092");
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 创建 Kafka 生产者对象，建立 Kafka 连接
        KafkaProducer<String, String> producer = new KafkaProducer<>(configMap);

        // 构建数据
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", "2", "hello kafka");

        // 生产 (发送) 数据
        producer.send(record);

        // 关闭生产者连接
        producer.close();
    }
}
