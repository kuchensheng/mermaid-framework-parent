package com.mermaid.framework.core.cloud;

import com.mermaid.framework.core.cloud.module.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * Desription:服务注册中心服务提供方
 * 服务方法registerProvider
 * 获取所有服务提供者信息方法getProviderServiceMap
 * @author:Hui CreateDate:2018/8/19 23:17
 * version 1.0
 */
public interface IRegisterCenter4Provider {

    /**
     * 服务端将服务提供者信息注册到ZK对应的节点下
     * @param servieMetaData
     */
    void registerProvider(final List<ProviderService> servieMetaData);

    /**
     * 服务端获取服务提供者信息
     * @return
     */
    Map<String,List<ProviderService>> getProviderServiceMap();

    void registerProvoder();
}
