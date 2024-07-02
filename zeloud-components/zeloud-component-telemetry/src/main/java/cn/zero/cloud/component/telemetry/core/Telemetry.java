package cn.zero.cloud.component.telemetry.core;

import java.lang.annotation.*;

/**
 * 方法切面日志注解
 *
 * @author Xisun Wang
 * @since 2024/3/14 11:06
 */
@Inherited
@Documented
@Target({ElementType.METHOD}) // 指定该注解可以用在方法上
@Retention(RetentionPolicy.RUNTIME) // 指定注解在运行时可见
public @interface Telemetry {
    String moduleName() default "";

    String metricName() default "";

    String featureName() default "";

    String verb() default "";

    String objectType() default "";

    String objectID() default "";

    String[] items() default {};
}
