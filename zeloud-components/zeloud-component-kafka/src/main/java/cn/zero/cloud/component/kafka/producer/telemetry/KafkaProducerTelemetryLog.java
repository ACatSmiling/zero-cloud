package cn.zero.cloud.component.kafka.producer.telemetry;

import cn.zero.cloud.component.kafka.common.telemetry.TelemetryConstants;
import cn.zero.cloud.component.kafka.common.telemetry.pojo.KafkaCommonTelemetryLog;

/**
 * @see <a href=
 * "https://wiki.cisco.com/display/HFWEB/Meeting+Notification+Telemetry+logs+of+Identity">Meeting
 * Notification Telemetry logs of Identity</a>
 */
public class KafkaProducerTelemetryLog extends KafkaCommonTelemetryLog {

    // The URL to retrieve detail data of current message object
    private String url;

    private String siteUUID;

    public KafkaProducerTelemetryLog() {
        super();
        this.setVerb(TelemetryConstants.VERB_PRODUCE);
    }

    public KafkaProducerTelemetryLog(String featureName) {
        this();
        this.setFeatureName(featureName);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSiteUUID() {
        return siteUUID;
    }

    public void setSiteUUID(String siteUUID) {
        this.siteUUID = siteUUID;
    }
}
