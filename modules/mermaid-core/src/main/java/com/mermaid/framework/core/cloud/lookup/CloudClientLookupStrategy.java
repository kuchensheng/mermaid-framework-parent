package com.mermaid.framework.core.cloud.lookup;

import com.mermaid.framework.core.application.ApplicationInfo;

import java.util.List;

/**
 * Desription:负载均衡算法策略
 *
 * @author:Hui CreateDate:2019/2/23 10:36
 * version 1.0
 */
public interface CloudClientLookupStrategy {

    ApplicationInfo select(List<ApplicationInfo> providerServices);
}
