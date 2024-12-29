package cn.zero.cloud.component.exception.config;

import cn.zero.cloud.component.exception.core.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Xisun Wang
 * @since 7/3/2024 16:53
 */
@AutoConfiguration
public class ZeloudExceptionAutoConfiguration {
    
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
