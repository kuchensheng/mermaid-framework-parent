package com.mermaid.framework.core.cloud.lookup;

import com.mermaid.framework.core.application.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Desription:加权随机算法在随机算法的基础上针对权重做了处理。
 *
 * @author:Hui CreateDate:2019/2/23 11:07
 * version 1.0
 */
public class WeightRandomCloudClientLookup extends RandomCloudClientLookup {

    @Override
    public ApplicationInfo select(List<ApplicationInfo> providerServices) {
        List<ApplicationInfo> providerList = new ArrayList<>();
        for (ApplicationInfo applicationInfo : providerServices) {
            int weight = applicationInfo.getWeight();
            for (int i=0;i<weight;i++) {
                providerList.add(applicationInfo);
            }
        }
        return super.select(providerList);
    }
}
