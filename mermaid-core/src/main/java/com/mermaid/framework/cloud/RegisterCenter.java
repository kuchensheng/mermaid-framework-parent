package com.mermaid.framework.cloud;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mermaid.framework.cloud.constant.CloudZonePattern;
import com.mermaid.framework.cloud.module.InvokerService;
import com.mermaid.framework.cloud.module.ProviderService;
import com.mermaid.framework.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Desription:
 * 注册中心实现
 * @author:Hui CreateDate:2018/8/19 23:16
 * version 1.0
 */
@Slf4j
@Configuration
@Component
public class RegisterCenter implements IRegisterCenter4Invoker,IRegisterCenter4Provider,InitializingBean,EmbeddedServletContainerCustomizer{

    private static RegisterCenter registerCenter = new RegisterCenter();

    private static final Map<String,List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

    private static final Map<String,List<ProviderService>> serviceMetaDataMap4Consumer = Maps.newConcurrentMap();

    @Autowired
    private ZkClient zkClient;

    @Value("${mermaid.zk.servers:118.178.186.33:2181}")
    private String zkServers;

    @Value("${mermaid.zk.timeout.session:30000}")
    private int sessionTimeout;

    @Value("${mermaid.zk.timeout.connection:60000}")
    private int connectionTimeout;

    @Value("${mermaid.zk.channel_size:5}")
    private int channelSize;


    @Autowired
    private Environment env;

    @Bean
    public ZkClient zkClient() {
        log.info("连接到ZK");
        ZkClient zkClient = new ZkClient(zkServers,sessionTimeout,connectionTimeout);
        return zkClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerProvoder();
    }

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
    public void registerProvoder() {
            log.info("注册为服务提供者");
            log.debug("-zk==========");
            //创建根节点
            if(!zkClient.exists(CloudZonePattern.ZNODE_APPLICATION)) {
                zkClient.createPersistent(CloudZonePattern.ZNODE_APPLICATION);
            }

            //创建服务节点
            String appName = env.getProperty("spring.application.name");
            if(!StringUtils.hasText(appName)) {

                    log.error("application.properties 文件中未设置spring.application.name值");
                    throw new RuntimeException("请设置spring.application.name属性值");

            }

            String port = env.getProperty("spring.application.index");
            if(!StringUtils.hasText(port)) {

                    log.error("未设置启动端口");
                    throw new RuntimeException("请设置服务启动端口");


            }else if(Integer.valueOf(port) < 1024 || Integer.valueOf(port) > 65535) {
                throw new RuntimeException("端口应设置在1024~65535之间");
            }

            String servicePath = CloudZonePattern.ZNODE_APPLICATION+"/"+appName;
            if(!zkClient.exists(servicePath)) {
                zkClient.createPersistent(servicePath);
            }

            //创建地址节点
            String addressPath = servicePath + CloudZonePattern.ZNODE_REALMS+"-"+port;
            if(!zkClient.exists(addressPath)) {
                zkClient.createEphemeral(addressPath);
            }else {
                log.error("端口{}已被占用",port);
                throw new RuntimeException("端口已被绑定，请切换端口");
            }
            log.info("服务注册成功，applicationName={},ip={},port={}",appName, IPUtil.getHostFirstIp(),port);

            log.info("监听服务变化");

    }

    @Override
    public void registerProvider(List<ProviderService> servieMetaData) {
        log.info("注册为服务提供者");
//        if(CollectionUtils.isEmpty(servieMetaData)){
//            log.error("集合为空");
//            throw new NullPointerException();
//        }
        log.info("连接ZK");
        synchronized (RegisterCenter.class) {
//            for(ProviderService provider : servieMetaData) {
//                String serviceIntfKey = provider.getServiceIntf().getName();
//
//                List<ProviderService> providers = providerServiceMap.get(serviceIntfKey);
//                if(null == providers) {
//                    providers = Lists.newArrayList();
//                }
//                providers.add(provider);
//                providerServiceMap.put(serviceIntfKey,providers);
//            }

            if(null == zkClient) {
                throw new RuntimeException();
            }

            String appName = System.getProperty("spring.application.name","mermaid");
            String appPort = System.getProperty("spring.application.index","8080");
            String path = "/"+appName+":"+appPort;
            log.info("添加节点{}",path);
            if(zkClient.exists(path)) {
                log.error("exist {}",path);
                throw new RuntimeException();
            }

            zkClient.createPersistent(path,Boolean.TRUE);

            log.info("创建服务提供者节点");

            for(Map.Entry<String,List<ProviderService>> entry : providerServiceMap.entrySet()) {
                String serviceNode = entry.getKey();
                String servicePath = path + "/" + serviceNode + "PROVIDER";
                if(!zkClient.exists(servicePath)) {
                    zkClient.createEphemeral(servicePath);
                }
            }

        }
    }

    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return null;
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        String port = env.getProperty("spring.application.index");
        if(StringUtils.hasText(port)) {
            configurableEmbeddedServletContainer.setPort(Integer.valueOf(port));
        }
    }
}


