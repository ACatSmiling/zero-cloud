package cn.zero.cloud.component.telemetry.core.filter.impl;

import cn.zero.cloud.component.telemetry.core.filter.FeatureNameFilter;
import cn.zero.cloud.component.telemetry.core.pojo.FeatureName;

import java.util.HashSet;
import java.util.Set;

/**
 * featureName 过滤器示例
 *
 * @author Xisun Wang
 * @since 2024/4/2 14:18
 */
public class ExampleFeatureNameFilter implements FeatureNameFilter {
    @Override
    public Set<FeatureName> getIgnoredFeatureName() {
        HashSet<FeatureName> featureNames = new HashSet<>();

        // 全局忽略指定 metricName 和 featureName 的 Telemetry 日志
        FeatureName featureName = new FeatureName("example", "example");
        featureNames.add(featureName);
        return featureNames;
    }
}
