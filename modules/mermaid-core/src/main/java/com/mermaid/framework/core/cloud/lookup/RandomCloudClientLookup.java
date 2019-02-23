package com.mermaid.framework.core.cloud.lookup;

import com.mermaid.framework.core.application.ApplicationInfo;
import org.apache.commons.lang.math.RandomUtils;

import java.util.List;

/**
 * Desription:客户端负载随机选择
 *
 * @author:Hui CreateDate:2019/2/23 10:37
 * version 1.0
 */
public class RandomCloudClientLookup implements CloudClientLookupStrategy {

    @Override
    public ApplicationInfo select(List<ApplicationInfo> providerServices) {
        int index = RandomUtils.nextInt(providerServices.size() -1);
        return providerServices.get(index);
    }

}
