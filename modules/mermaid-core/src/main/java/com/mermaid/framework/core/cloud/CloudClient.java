package com.mermaid.framework.core.cloud;

import com.google.common.collect.Maps;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.common.CloudZnodePattern;
import com.mermaid.framework.core.cloud.constant.CloudZonePattern;
import org.I0Itec.zkclient.ZkClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/20 22:56
 * version 1.0
 */
public class CloudClient {

    //服务提供者列表，key=服务名称，
    private static final Map<String,List<ApplicationInfo>> providerMap = Maps.newConcurrentMap();

    private String zkServers;
    
    private int connectionTimeout;
    
    private int sessionTimeout;

    public CloudClient(String zkServers,int connectionTimeout,int sessionTimeout) {
        this.zkServers = zkServers;
        this.connectionTimeout = connectionTimeout;
        this.sessionTimeout = sessionTimeout;
    }
    public void connect() {
        register2Zookeeper(new ZkClient(zkServers,sessionTimeout,connectionTimeout));
    }

    private void register2Zookeeper(ZkClient zkClient) {
        String path = CloudZnodePattern.ZNODE_INSTANCE_GROUP.replace("${appName}",ApplicationInfo.getInstance().getAppName());
        if(!zkClient.exists(path)) {
            zkClient.createPersistent(path,Boolean.TRUE);
        }

        String nodePath = CloudZnodePattern.ZNODE_INSTANCE_INFORMATION.replace("${appName}",ApplicationInfo.getInstance().getAppName()).replace("${appId}",ApplicationInfo.getInstance().getAppId());
        if(!zkClient.exists(nodePath)) {
            zkClient.createEphemeral(nodePath,ApplicationInfo.getInstance());
            addProvider(ApplicationInfo.getInstance().getAppName(),ApplicationInfo.getInstance());
        }else {
            throw new RuntimeException("端口已被占用");
        }
    }

    private void addProvider(String key,ApplicationInfo applicationInfo) {
        if(null == providerMap.get(key)) {
            providerMap.put(key,new ArrayList<ApplicationInfo>());
        }
        providerMap.get(key).add(applicationInfo);
    }


}
