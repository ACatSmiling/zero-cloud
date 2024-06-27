package cn.zero.cloud.platform.filter.impl;

import cn.zero.cloud.platform.filter.FeatureNameFilter;
import cn.zero.cloud.platform.pojo.FeatureName;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Xisun Wang
 * @since 2024/4/2 14:18
 */
public class GlobalFeatureNameFilter implements FeatureNameFilter {
    @Override
    public Set<FeatureName> getIgnoredFeatureName() {
        HashSet<FeatureName> featureNames = new HashSet<>();
        FeatureName featureName = new FeatureName("aaa", "bbb");
        featureNames.add(featureName);
        return featureNames;
    }
}
