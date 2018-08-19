package com.mermaid.framework.config;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/20 0:09
 * version 1.0
 */
@Configuration
public class ZkServiceConfiguration {

    @Value("${mermaid.zk.servers:118.178.186:2181}")
    private String zkServers;

    @Value("${mermaid.zk.timeout.session:1000}")
    private int sessionTimeout;

    @Value("${mermaid.zk.timeout.connection:3000}")
    private int connectionTimeout;

    @Value("${mermaid.zk.channel_size:5}")
    private int channelSize;

    @Value("${mermaid.zk.serialize.type:HessianSerializer}")
    private ZkSerializer zkSerializer;

    @Bean
    public ZkClient zkClient() {
        ZkClient zkClient = new ZkClient(zkServers,sessionTimeout,connectionTimeout,zkSerializer);
        return zkClient;
    }
}
