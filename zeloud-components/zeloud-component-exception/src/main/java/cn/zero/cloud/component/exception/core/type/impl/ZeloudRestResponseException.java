package cn.zero.cloud.component.exception.core.type.impl;

import cn.zero.cloud.component.exception.core.type.CommonException;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/26 16:04
 */
public class ZeloudRestResponseException extends RuntimeException implements CommonException {

    @Serial
    private static final long serialVersionUID = 7334456646535372527L;

    private final int status;

    private static final String KEY = "description_";

    private Map<String, String> additionalMessages;

    public ZeloudRestResponseException(int status, String message) {
        super(message);
        this.status = status;
    }

    public ZeloudRestResponseException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    @Override
    public void addAdditionalMessage(String description) {
        if (additionalMessages == null) {
            additionalMessages = new LinkedHashMap<>();
        }
        int index = additionalMessages.size() + 1;
        additionalMessages.put(KEY + index, description);
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getAdditionalMessages() {
        return additionalMessages;
    }
}
