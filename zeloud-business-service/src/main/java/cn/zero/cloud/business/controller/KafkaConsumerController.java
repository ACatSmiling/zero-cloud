package cn.zero.cloud.business.controller;

import cn.zero.cloud.component.kafka.common.pojo.healthcheck.HealthCheckSummary;
import cn.zero.cloud.component.kafka.common.pojo.healthcheck.HealthCheckType;
import cn.zero.cloud.component.kafka.consumer.healthcheck.KafkaConsumerHealthCheckContainerService;
import cn.zero.cloud.platform.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static cn.zero.cloud.platform.constants.TelemetryConstants.FeatureType.KAFKA_FEATURE;
import static cn.zero.cloud.platform.constants.TelemetryConstants.MetricType.KAFKA_METRIC;
import static cn.zero.cloud.platform.constants.TelemetryConstants.ModuleType.KAFKA_API;
import static cn.zero.cloud.platform.constants.TelemetryConstants.ObjectType.KAFKA_OBJECT;

/**
 * @author Xisun Wang
 * @since 4/30/2024 09:32
 */
@RestController
@RequestMapping(value = "/kafka/consumer")
public class KafkaConsumerController {
    private final KafkaConsumerHealthCheckContainerService kafkaConsumerHealthCheckContainerService;

    @Autowired
    public KafkaConsumerController(KafkaConsumerHealthCheckContainerService kafkaConsumerHealthCheckContainerService) {
        this.kafkaConsumerHealthCheckContainerService = kafkaConsumerHealthCheckContainerService;
    }

    @Telemetry(moduleType = KAFKA_API, metricType = KAFKA_METRIC, featureType = KAFKA_FEATURE, objectType = KAFKA_OBJECT)
    @GetMapping(value = "/healthcheck/legacy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public HealthCheckSummary legacyHealthCheck() {
        return kafkaConsumerHealthCheckContainerService.checkDownstreamComponents(HealthCheckType.LEGACY_HEALTH_CHECK);
    }

    @Telemetry(moduleType = KAFKA_API, metricType = KAFKA_METRIC, featureType = KAFKA_FEATURE, objectType = KAFKA_OBJECT)
    @GetMapping(value = "/healthcheck/liveness", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public HealthCheckSummary livenessHealthCheck() {
        return kafkaConsumerHealthCheckContainerService.checkDownstreamComponents(HealthCheckType.LIVENESS_CHECK);
    }

    @Telemetry(moduleType = KAFKA_API, metricType = KAFKA_METRIC, featureType = KAFKA_FEATURE, objectType = KAFKA_OBJECT)
    @GetMapping(value = "/healthcheck/readiness", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public HealthCheckSummary readinessHealthCheck() {
        return kafkaConsumerHealthCheckContainerService.checkDownstreamComponents(HealthCheckType.READINESS_CHECK);
    }
}
