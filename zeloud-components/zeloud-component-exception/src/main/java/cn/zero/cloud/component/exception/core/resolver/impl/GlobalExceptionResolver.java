package cn.zero.cloud.component.exception.core.resolver.impl;

import cn.zero.cloud.component.exception.core.resolver.AbstractExceptionResolver;
import cn.zero.cloud.component.general.tool.utils.ZeloudDateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author Xisun Wang
 * @since 2024/3/26 16:06
 */
public class GlobalExceptionResolver extends AbstractExceptionResolver {

    @Override
    protected String getExceptionResolverName() {
        return "globalExceptionResolver";
    }

    @Override
    public ResponseEntity<Object> getResponseEntity(Exception e) {
        Map<String, Object> body = generateMessageBody(e.getMessage());
        body.put(TIMESTAMP, ZeloudDateUtil.getCurrentTimeDefaultTimeZone());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
