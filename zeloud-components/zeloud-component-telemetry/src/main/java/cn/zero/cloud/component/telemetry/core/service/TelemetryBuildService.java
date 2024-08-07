package cn.zero.cloud.component.telemetry.core.service;

import cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.core.pojo.TelemetryLog;
import cn.zero.cloud.component.telemetry.core.decorator.TelemetryDecorator;
import cn.zero.cloud.component.telemetry.core.expression.ExpressionEvaluatorUtil;
import cn.zero.cloud.component.telemetry.core.factory.TelemetryDecoratorFactory;
import cn.zero.cloud.component.telemetry.core.Telemetry;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xisun Wang
 * @since 2024/3/14 16:14
 */
public class TelemetryBuildService {
    private static final String RETRY_NUMBER = "retryNumber";

    private static final Class<? extends Annotation>[] mappingAnnotations = new Class[]
            {RequestMapping.class, PutMapping.class, GetMapping.class, PostMapping.class, DeleteMapping.class};

    public TelemetryLog buildBasicTelemetryLog(ProceedingJoinPoint joinPoint, Telemetry telemetry) throws Exception {
        TelemetryLog telemetryLog = new TelemetryLog();

        addBasicRequestInfo(telemetryLog);
        addBasicMappingInfo(joinPoint, telemetryLog);
        addBasicMethodInfo(joinPoint, telemetryLog);

        if (telemetry.moduleName() != null && StringUtils.isNotBlank(telemetry.moduleName())) {
            telemetryLog.setModuleName(telemetry.moduleName());
        }

        if (telemetry.metricName() != null && StringUtils.isNotBlank(telemetry.metricName())) {
            telemetryLog.setMetricName(telemetry.metricName());
        }

        if (telemetry.featureName() != null && StringUtils.isNotBlank(telemetry.featureName())) {
            telemetryLog.setFeatureName(telemetry.featureName());
        }

        if (telemetry.objectType() != null && StringUtils.isNotBlank(telemetry.objectType())) {
            telemetryLog.setObjectType(telemetry.objectType());
        }

        String objectID = telemetry.objectID();
        if (StringUtils.isNotBlank(objectID)) {
            if (objectID.contains("#")) {
                telemetryLog.setObjectID(ExpressionEvaluatorUtil.getString(joinPoint, objectID));
            } else {
                telemetryLog.setObjectID(objectID);
            }
        }

        for (String item : telemetry.items()) {
            if (item.contains("#")) {
                String key = item.replace("#", "");
                if (key.contains(".")) {
                    String[] split = item.split("\\.");
                    key = split[split.length - 1];
                }
                telemetryLog.setItem(key, ExpressionEvaluatorUtil.getString(joinPoint, item));
            }
        }
        return telemetryLog;
    }

    public TelemetryLog decorateTelemetryLog(ProceedingJoinPoint joinPoint, Telemetry telemetry, TelemetryLog telemetryLog, Object result) {
        Object[] joinPointArgs = joinPoint.getArgs();
        if (joinPointArgs != null) {
            // add retryNumber flag
            String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            // get parameter "retryNumber" index
            int isRetryIndex = ArrayUtils.indexOf(argNames, RETRY_NUMBER);
            if (isRetryIndex > -1) {
                int retryNumber = (int) joinPointArgs[isRetryIndex];
                telemetryLog.setItem(RETRY_NUMBER, retryNumber);
            }
        }

        // 根据 ObjectType，获取已注册的 TelemetryDecorator，执行对应特定的日志处理
        String objectType = telemetry.objectType();
        if (StringUtils.isNotBlank(objectType) && TelemetryDecoratorFactory.getService(objectType) != null) {
            TelemetryDecorator decorator = TelemetryDecoratorFactory.getService(objectType);
            decorator.buildTelemetryItems(result, telemetryLog);
        }
        return telemetryLog;
    }

    private void addBasicRequestInfo(TelemetryLog telemetryLog) {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return;
        }
        telemetryLog.setRequestType(request.getMethod());
        telemetryLog.setRequestUrl(request.getRequestURL().toString());
    }

    private void addBasicMappingInfo(ProceedingJoinPoint joinPoint, TelemetryLog telemetryLog) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        telemetryLog.setClassMapping(resolveClassMapping(method));
        telemetryLog.setMethodMapping(resolveMethodMapping(method));
    }

    private void addBasicMethodInfo(ProceedingJoinPoint joinPoint, TelemetryLog telemetryLog) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        telemetryLog.setFullPath(method.toString());
        telemetryLog.setMethodName(method.getName());
    }

    public HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return servletRequestAttributes == null ? null : servletRequestAttributes.getRequest();
    }

    private String resolveClassMapping(Method method) throws Exception {
        if (method == null) {
            throw new Exception(TelemetryConstants.AOP_METHOD_IS_NULL);
        }

        Class<?> controllerClass = method.getDeclaringClass();
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            String[] classMapping = controllerClass.getAnnotation(RequestMapping.class).value();
            if (classMapping.length > 1) {
                return TelemetryConstants.MULTIPLE_VALUES;
            }
            return StringUtils.join(classMapping);
        }
        return null;
    }

    public String resolveMethodMapping(Method method) throws Exception {
        if (method == null) {
            throw new Exception(TelemetryConstants.AOP_METHOD_IS_NULL);
        }

        List<String> methodMappings = new ArrayList<>();
        for (Class<? extends Annotation> annotationClass : mappingAnnotations) {
            if (method.isAnnotationPresent(annotationClass)) {
                Annotation annotation = method.getAnnotation(annotationClass);
                String[] methodMapping = getMethodMapping(annotation);
                if (methodMapping != null && methodMapping.length > 1) {
                    return TelemetryConstants.MULTIPLE_VALUES;
                }
                methodMappings.add(StringUtils.join(methodMapping));
            }
        }

        if (methodMappings.size() == 1) {
            return methodMappings.get(0);
        } else if (methodMappings.size() > 1) {
            return TelemetryConstants.MULTIPLE_MAPPINGS;
        }
        return null;
    }

    private String[] getMethodMapping(Annotation annotation) {
        if (annotation == null) {
            return null;
        }

        String[] mappings = null;
        if (annotation instanceof RequestMapping) {
            mappings = ((RequestMapping) annotation).value();
        } else if (annotation instanceof GetMapping) {
            mappings = ((GetMapping) annotation).value();
        } else if (annotation instanceof PostMapping) {
            mappings = ((PostMapping) annotation).value();
        } else if (annotation instanceof PutMapping) {
            mappings = ((PutMapping) annotation).value();
        } else if (annotation instanceof DeleteMapping) {
            mappings = ((DeleteMapping) annotation).value();
        }
        return mappings;
    }
}
