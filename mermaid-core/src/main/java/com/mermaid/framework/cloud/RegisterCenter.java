package com.mermaid.framework.cloud;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mermaid.framework.cloud.module.InvokerService;
import com.mermaid.framework.cloud.module.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Desription:
 * 注册中心实现
 * @author:Hui CreateDate:2018/8/19 23:16
 * version 1.0
 */
@Slf4j
@Component
public class RegisterCenter implements IRegisterCenter4Invoker,IRegisterCenter4Provider{

    private static RegisterCenter registerCenter = new RegisterCenter();

    private static final Map<String,List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

    private static final Map<String,List<ProviderService>> serviceMetaDataMap4Consumer = Maps.newConcurrentMap();

    @Autowired
    private ZkClient zkClient;

    @Value("${spring.application.name:mermaid}")
    private String appName;
    @Value("${spring.application.index:8080}")
    private String appPort;

    @Override
    public void initProviderMap() {

    }

    @Override
    public Map<String, List<ProviderService>> getServiceMetaDataMap4Consumer() {
        return null;
    }

    @Override
    public void registerInvoker(InvokerService invokerService) {

    }

    @Override
    public void registerProvider(List<ProviderService> servieMetaData) {
        log.info("注册为服务提供者");
        if(CollectionUtils.isEmpty(servieMetaData)){
            log.error("集合为空");
            throw new NullPointerException();
        }
        log.info("连接ZK");
        synchronized (RegisterCenter.class) {
            for(ProviderService provider : servieMetaData) {
                String serviceIntfKey = provider.getServiceIntf().getName();

                List<ProviderService> providers = providerServiceMap.get(serviceIntfKey);
                if(null == providers) {
                    providers = Lists.newArrayList();
                }
                providers.add(provider);
                providerServiceMap.put(serviceIntfKey,providers);
            }

            if(null == zkClient) {
                throw new RuntimeException();
            }

            String path = "/"+appName+":"+appPort;
            if(zkClient.exists(path)) {
                log.error("exist {}",path);
                throw new RuntimeException();
            }

            zkClient.createPersistent(path,Boolean.TRUE);

            log.info("创建服务提供者节点");

            zkClient.createPersistent(path);

            for(Map.Entry<String,List<ProviderService>> entry : providerServiceMap.entrySet()) {
                String serviceNode = entry.getKey();
                String servicePath = path + "/" + serviceNode + "PROVIDER";
                zkClient.exists(servicePath);
            }

        }
    }

    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return null;
    }
}
