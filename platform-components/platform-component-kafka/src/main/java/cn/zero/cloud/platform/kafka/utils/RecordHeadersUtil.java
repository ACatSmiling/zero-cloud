package cn.zero.cloud.platform.kafka.utils;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.nio.charset.StandardCharsets;

public class RecordHeadersUtil {
    private RecordHeadersUtil() {
        throw new IllegalStateException("Utility class!");
    }

    public static String getValueFromHeader(Headers headers, String key) {
        if (null != headers) {
            Header header = headers.lastHeader(key);
            if (header != null) {
                return new String(header.value(), StandardCharsets.UTF_8);
            }
            return "";
        }
        return "";
    }
}
