package cn.zero.cloud.component.telemetry.core.pojo;

/**
 * @author Xisun Wang
 * @since 2024/3/21 14:38
 */
public record FeatureName(String metricName, String featureName) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            FeatureName that = (FeatureName) obj;
            return this.metricName.equals(that.metricName) && this.featureName.equals(that.featureName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return " metricName: " + this.metricName + ",featureName: " + this.featureName;
    }
}
