package com.mermaid.framework.core.cloud.lookup;

import com.mermaid.framework.core.application.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/23 11:25
 * version 1.0
 */
public class WeightPollingCloudClientLookup extends PollingCloudClientLookup {

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
