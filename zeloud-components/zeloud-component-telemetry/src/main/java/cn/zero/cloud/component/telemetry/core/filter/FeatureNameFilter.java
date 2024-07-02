package cn.zero.cloud.component.telemetry.core.filter;

import cn.zero.cloud.component.telemetry.core.pojo.FeatureName;

import java.util.Set;

/**
 * featureName 过滤器
 *
 * @author Xisun Wang
 * @since 2024/3/21 14:40
 */
public interface FeatureNameFilter {
    Set<FeatureName> getIgnoredFeatureName();
}
