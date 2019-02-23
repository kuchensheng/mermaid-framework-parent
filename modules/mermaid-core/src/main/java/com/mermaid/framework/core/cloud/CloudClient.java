package com.mermaid.framework.core.cloud;

import com.google.common.collect.Maps;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.common.CloudZnodePattern;
import com.mermaid.framework.serialize.JsonSerializer;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/20 22:56
 * version 1.0
 */
public class CloudClient {

    private static final Logger logger = LoggerFactory.getLogger(CloudClient.class);

    //服务提供者列表，key=服务名称，
    private static final Map<String,List<ApplicationInfo>> providerMap = Maps.newConcurrentMap();

    private String zkServers;
    
    private int connectionTimeout;

    private ZkClient zkClient;

    private ApplicationInfo applicationInfo = ApplicationInfo.getInstance();

    public CloudClient(Properties properties) {
        this.zkServers = properties.getProperty("mermaid.zookeeper.connection","192.168.1.4:2181");
        this.connectionTimeout = Integer.parseInt(properties.getProperty("mermaid.zookeeper.connectionTimeout","60"));
    }
    public void connect() {
        logger.info("注册到zookeeper，servers={}",zkServers);
        try {
            this.zkClient = new ZkClient(zkServers,connectionTimeout);
            register2Zookeeper();
        } catch (Exception e) {
            logger.error("zk 注册失败",e);
            throw new RuntimeException(e);
        }
    }

    private void register2Zookeeper() {
        final String path = CloudZnodePattern.ZNODE_INSTANCE_GROUP.replace("${appName}",applicationInfo.getAppName());
        if(!zkClient.exists(path)) {
            logger.info("创建父节点");
            zkClient.createPersistent(path,Boolean.TRUE);
        }

        final String nodePath = CloudZnodePattern.ZNODE_INSTANCE_INFORMATION.replace("${appName}",applicationInfo.getAppName()).replace("${appId}",applicationInfo.getAppId());
        if(!zkClient.exists(nodePath)) {
            initProviderMap();
            logger.info("创建临时节点");
            zkClient.createEphemeral(nodePath, new JsonSerializer().serialize(applicationInfo));
            providerMap.put(path, Arrays.asList(applicationInfo));
            /**注册监听服务的变化，同时更新到本地缓存*/
            zkClient.subscribeChildChanges(nodePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    refreshInstance(path);
                    logger.info("有节点变化了,providerMap.size={}",providerMap.size());
                }
            });
        }else {
            logger.error("服务{},端口{}被占用",applicationInfo.getAppName(),applicationInfo.getAppId());
            throw new RuntimeException("端口已被占用");
        }

        zkClient.subscribeDataChanges(nodePath, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                logger.info("节点数据有变化，{}","datapath="+dataPath+"\tdata="+String.valueOf(data));
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        });

        logger.info("应用[{}]的实例[{}]已上线",applicationInfo.getAppName(),applicationInfo.getAppId());
    }

    private void initProviderMap() {
        logger.info("初始化存储服务清单");
        if(!CollectionUtils.isEmpty(providerMap)) {
            providerMap.clear();
        }
        List<String> services = zkClient.getChildren(CloudZnodePattern.ZNODE_INSTANCE);
        if(!services.isEmpty()) {
            for (String serviceNode: services) {
                refreshInstance(CloudZnodePattern.ZNODE_INSTANCE+"/"+serviceNode);
            }

        }

    }

    private void refreshInstance(String path) {
        List<String> children = zkClient.getChildren(path);
        if(!children.isEmpty()) {
            //更新本地缓存
            List<ApplicationInfo> applicationInfos = new ArrayList<>();
            for (String child : children) {
                applicationInfos.add((ApplicationInfo)zkClient.readData(child));
            }
            providerMap.put(path,applicationInfos);
        }
    }


}
