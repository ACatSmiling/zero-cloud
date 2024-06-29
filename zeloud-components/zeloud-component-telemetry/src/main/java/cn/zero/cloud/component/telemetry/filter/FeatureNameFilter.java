package cn.zero.cloud.component.telemetry.filter;


import cn.zero.cloud.component.telemetry.pojo.FeatureName;

import java.util.Set;

/**
 * @author Xisun Wang
 * @since 2024/3/21 14:40
 */
public interface FeatureNameFilter {
    Set<FeatureName> getIgnoredFeatureName();
}
