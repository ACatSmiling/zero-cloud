package cn.zero.cloud.platform.filter;


import cn.zero.cloud.platform.pojo.FeatureName;

import java.util.Set;

/**
 * @author Xisun Wang
 * @since 2024/3/21 14:40
 */
public interface FeatureNameFilter {
    Set<FeatureName> getIgnoredFeatureName();
}
