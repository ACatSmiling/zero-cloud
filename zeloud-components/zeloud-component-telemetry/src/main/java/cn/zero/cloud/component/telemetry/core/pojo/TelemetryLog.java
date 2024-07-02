package cn.zero.cloud.component.telemetry.core.pojo;

import cn.zero.cloud.component.telemetry.core.constants.TelemetryConstants;
import cn.zero.cloud.component.telemetry.core.factory.TelemetryLoggerFactory;
import cn.zero.cloud.component.telemetry.core.logger.TelemetryLogger;
import cn.zero.cloud.component.general.tool.utils.ZeloudDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Xisun Wang
 * @since 2024/3/14 11:22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelemetryLog {
    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * class mapping
     */
    private String classMapping;

    /**
     * method mapping
     */
    private String methodMapping;

    /**
     * 方法全路径
     */
    private String fullPath;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 特性名称
     */
    private String featureName;

    /**
     * 动作类型
     */
    private String verb;

    /**
     * 对象类型
     */
    private String objectType;

    /**
     * 对象主键
     */
    private String objectID;

    /**
     * 附加参数
     */
    private Map<String, Object> items;

    /**
     * 方法处理结果
     */
    private String processResult;

    /**
     * 方法处理时间
     */
    private long processTime;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * ThreadLocal 中的一个随机 UUID，无实际意义
     */
    private String randomUUID;

    /**
     * 当前日志的生成时间
     */
    private final String timestamp = ZeloudDateUtil.getCurrentTimeDefaultTimeZone();

    /**
     * 设置指定的附加参数
     *
     * @param name  附加参数名
     * @param value 附加参数值
     */
    public void setItem(String name, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String && StringUtils.isBlank((String) value)) {
            return;
        }

        Map<String, Object> m = this.getItems();
        if (m == null) {
            m = new HashMap<>();
        }
        m.put(name, value);

        this.setItems(m);
    }

    public void validate() throws IllegalArgumentException {
        if (StringUtils.isBlank(this.metricName) || StringUtils.isBlank(this.featureName)) {
            throw new IllegalArgumentException("metricType and featureType MUST have value for telemetry log!");
        }
    }

    public void logInfo() {
        TelemetryLogger telemetryLogger = TelemetryLoggerFactory.getTelemetryLogger();
        telemetryLogger.info(this);
    }

    public void logForSuccess() {
        logWithResultAndMessage(TelemetryConstants.ProcessResult.SUCCESS, null);
    }

    public void logForFailure(String errorMsg) {
        logWithResultAndMessage(TelemetryConstants.ProcessResult.FAILURE, errorMsg);
    }

    private void logWithResultAndMessage(TelemetryConstants.ProcessResult processResult, String errorMsg) {
        TelemetryLogger telemetryLogger = TelemetryLoggerFactory.getTelemetryLogger();
        this.setProcessResult(processResult.getName());
        if (StringUtils.isNotBlank(errorMsg)) {
            this.setErrorMsg(errorMsg);
        }
        telemetryLogger.info(this);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getClassMapping() {
        return classMapping;
    }

    public void setClassMapping(String classMapping) {
        this.classMapping = classMapping;
    }

    public String getMethodMapping() {
        return methodMapping;
    }

    public void setMethodMapping(String methodMapping) {
        this.methodMapping = methodMapping;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public Map<String, Object> getItems() {
        return items;
    }

    public void setItems(Map<String, Object> items) {
        this.items = items;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRandomUUID() {
        return randomUUID;
    }

    public void setRandomUUID(String randomUUID) {
        this.randomUUID = randomUUID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelemetryLog that = (TelemetryLog) o;
        return processTime == that.processTime && Objects.equals(requestType, that.requestType) && Objects.equals(requestUrl, that.requestUrl)
                && Objects.equals(classMapping, that.classMapping) && Objects.equals(methodMapping, that.methodMapping)
                && Objects.equals(fullPath, that.fullPath) && Objects.equals(methodName, that.methodName) && Objects.equals(moduleName, that.moduleName)
                && Objects.equals(metricName, that.metricName) && Objects.equals(featureName, that.featureName) && Objects.equals(verb, that.verb)
                && Objects.equals(objectType, that.objectType) && Objects.equals(objectID, that.objectID) && Objects.equals(items, that.items)
                && Objects.equals(processResult, that.processResult) && Objects.equals(errorMsg, that.errorMsg) && Objects.equals(randomUUID, that.randomUUID)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestType, requestUrl, classMapping, methodMapping, fullPath, methodName, moduleName, metricName, featureName, verb,
                objectType, objectID, items, processResult, processTime, errorMsg, randomUUID, timestamp);
    }

    @Override
    public String toString() {
        return "TelemetryLog{" +
                "requestType='" + requestType + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", classMapping='" + classMapping + '\'' +
                ", methodMapping='" + methodMapping + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", methodName='" + methodName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", metricName='" + metricName + '\'' +
                ", featureName='" + featureName + '\'' +
                ", verb='" + verb + '\'' +
                ", objectType='" + objectType + '\'' +
                ", objectID='" + objectID + '\'' +
                ", items=" + items +
                ", processResult='" + processResult + '\'' +
                ", processTime=" + processTime +
                ", errorMsg='" + errorMsg + '\'' +
                ", randomUUID='" + randomUUID + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
