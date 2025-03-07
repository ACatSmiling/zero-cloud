package cn.zero.cloud.component.exception.core.type.impl;

import cn.zero.cloud.component.exception.core.type.CommonException;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 返回 Json 响应体时使用
 *
 * @author Xisun Wang
 * @since 2024/3/26 17:03
 */
public class ZeloudJsonException extends RuntimeException implements CommonException {

    @Serial
    private static final long serialVersionUID = -7389426743866292100L;

    private final int status;

    private static final String KEY = "description_";

    private Map<String, String> additionalMessages;

    public ZeloudJsonException(int status, String message) {
        super(message);
        this.status = status;
    }

    public ZeloudJsonException(int status, String message, Throwable cause) {
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
