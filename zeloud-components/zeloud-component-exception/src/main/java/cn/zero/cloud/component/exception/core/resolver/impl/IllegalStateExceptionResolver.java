package cn.zero.cloud.component.exception.core.resolver.impl;

import cn.zero.cloud.component.exception.core.resolver.AbstractExceptionResolver;
import cn.zero.cloud.component.general.tool.utils.ZeloudDateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/26 16:14
 */
public class IllegalStateExceptionResolver extends AbstractExceptionResolver {

    @Override
    protected String getExceptionResolverName() {
        return "illegalStateExceptionResolver";
    }

    @Override
    public ResponseEntity<Object> getResponseEntity(Exception e) {
        IllegalStateException ex = (IllegalStateException) e;
        Map<String, Object> body = generateMessageBody(ex.getMessage());
        addAdditionalMessage(body, ex.getLocalizedMessage());
        addAdditionalMessage(body, ex.getLocalizedMessage());
        addAdditionalMessage(body, ex.getLocalizedMessage());
        body.put(TIMESTAMP, ZeloudDateUtil.getCurrentTimeDefaultTimeZone());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}