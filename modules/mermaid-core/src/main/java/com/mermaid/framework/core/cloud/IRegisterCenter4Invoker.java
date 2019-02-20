package com.mermaid.framework.core.cloud;

import com.mermaid.framework.core.cloud.module.InvokerService;
import com.mermaid.framework.core.cloud.module.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * Desription:注册中心服务消费方
 * 主要方法有：
 * 初始化服务提供者信息本地缓存
 * 消费端获取服务提供者信息
 * 消费端将消费者信息注册到ZK
 * @author:Hui CreateDate:2018/8/19 23:49
 * version 1.0
 */
public interface IRegisterCenter4Invoker {

    /**
     * 消费端初始化服务提供者信息本地缓存
     */
    void initProviderMap();

    /**
     * 消费端获取服务提供者信息
     * @return
     */
    Map<String,List<ProviderService>> getServiceMetaDataMap4Consumer();

    /**
     * 消费端将消费者信息注册到ZK
     * @param invokerService
     */
    void registerInvoker(final InvokerService invokerService);
}
