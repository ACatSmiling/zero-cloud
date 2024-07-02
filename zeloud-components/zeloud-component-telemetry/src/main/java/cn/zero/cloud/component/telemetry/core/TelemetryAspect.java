package cn.zero.cloud.component.telemetry.core;

import cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.core.pojo.TelemetryLog;
import cn.zero.cloud.component.telemetry.core.service.TelemetryBuildService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import java.util.UUID;

/**
 * @author Xisun Wang
 * @since 2024/3/14 11:16
 */
@Aspect
@Order(1)
public class TelemetryAspect {
    private final TelemetryBuildService telemetryBuildService;

    public TelemetryAspect(TelemetryBuildService telemetryBuildService) {
        this.telemetryBuildService = telemetryBuildService;
    }

    @Around("@annotation(telemetry)") // 指定切点表达式
    public Object handle(ProceedingJoinPoint joinPoint, Telemetry telemetry) throws Throwable {
        // 随机生成一个 UUID，放入 ThreadLocal 中，无实际意义
        MDC.put(TelemetryConstants.UUID_KEY_EXAMPLE, UUID.randomUUID().toString());

        // 切面方法执行前，以切面默认的值，对切面日志各属性赋值
        long beginTime = System.currentTimeMillis();
        String resultStaus = TelemetryConstants.ProcessResult.SUCCESS.getName();
        TelemetryLog telemetryLog = telemetryBuildService.buildBasicTelemetryLog(joinPoint, telemetry);
        try {
            // 继续执行切面方法业务逻辑
            Object result = joinPoint.proceed();
            // 切面方法正常执行后，根据已定义并注册的 TelemetryDecorate，对日志进行额外的处理
            telemetryLog = telemetryBuildService.decorateTelemetryLog(joinPoint, telemetry, telemetryLog, result);
            return result;
        } catch (Exception e) {
            // 切面方法的异常处理
            resultStaus = TelemetryConstants.ProcessResult.FAILURE.getName();
            telemetryLog.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            // 切面方法完成，日志中添加处理时间和处理结果
            long endTime = System.currentTimeMillis();
            telemetryLog.setProcessTime(endTime - beginTime);
            telemetryLog.setProcessResult(resultStaus);

            // 输出日志
            telemetryLog.logInfo();
        }
    }
}
