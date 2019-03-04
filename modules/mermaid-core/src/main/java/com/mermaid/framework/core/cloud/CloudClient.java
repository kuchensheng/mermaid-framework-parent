package com.mermaid.framework.core.cloud;

import com.google.common.collect.Maps;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.common.CloudZnodePattern;
import com.mermaid.framework.core.cloud.common.Span;
import com.mermaid.framework.core.cloud.common.ThreadLocalProcessorTracer;
import com.mermaid.framework.serialize.HessianSeializer;
import com.mermaid.framework.serialize.ISerializer;
import org.I0Itec.zkclient.*;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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

    private IZkConnection zkConnection;

    private ZkSerializer zkSerializer;

    private ApplicationInfo applicationInfo = ApplicationInfo.getInstance();

    private BlockingQueue<Span>  spanBlockingQueue = null;

    public CloudClient(Properties properties) {
        this.zkServers = properties.getProperty("mermaid.zookeeper.connection","10.190.35.123:2181");
        this.connectionTimeout = Integer.parseInt(properties.getProperty("mermaid.zookeeper.connectionTimeout","60000"));
    }
    public void connect() {
        logger.info("注册到zookeeper，servers={}",zkServers);
        try {
            this.zkConnection = new ZkConnection(zkServers);
            this.zkClient = new ZkClient(zkConnection,connectionTimeout);
            this.zkSerializer = new SerializableSerializer();
            this.zkClient.addAuthInfo("digest","sweetCloud:sweetCloud123".getBytes());
            register2Zookeeper();
            applicationInfo.setCloudClient(this);
            initializeQueueInfo();
        } catch (Exception e) {
            logger.error("zk 注册失败",e);
            throw new RuntimeException(e);
        }
    }

    private void initializeQueueInfo() {
        String queueSize = System.getProperty("mermaid.request.trace.long.queue.size","10000");
        logger.info("正在启动请求跟踪日志队列[size={}]的写入线程",queueSize);
        spanBlockingQueue = new LinkedBlockingQueue<>(Integer.parseInt(queueSize));
        final ISerializer serializer = new HessianSeializer();
        try {
            Span take = spanBlockingQueue.take();
            write2Mq(serializer.serialize(take));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void write2Mq(byte[] serialize) {

    }

    private void register2Zookeeper() {
        final String path = CloudZnodePattern.ZNODE_INSTANCE_GROUP.replace("${appName}",applicationInfo.getAppName());
        if(!zkClient.exists("/applications")) {
            logger.info("创建父节点");

            zkClient.createPersistent("/applications");
            if(!zkClient.exists("/applications/"+applicationInfo.getAppName())) {
                zkClient.createPersistent("/applications/"+applicationInfo.getAppName());
            }
        }

        initProviderMap();

        /**注册监听服务的变化，同时更新到本地缓存*/
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                logger.info("节点服务[{}]的实例有变动，更新该服务实例列表信息",path);
                refreshInstance(path);
                logger.info("更新完成,providerMap.size={}",providerMap.size());
            }
        });

        final String nodePath = CloudZnodePattern.ZNODE_INSTANCE_INFORMATION.replace("${appName}",applicationInfo.getAppName()).replace("${appId}",applicationInfo.getAppId());

        if(!zkClient.exists(nodePath)) {
            logger.info("创建临时节点");
            try {
                zkClient.createEphemeral(nodePath,applicationInfo);
            } catch (RuntimeException e) {
                logger.error("创建临时节点失败",e);
                throw new RuntimeException(e);
            }
        }else {
            logger.error("服务[{}],端口[{}]被占用",applicationInfo.getAppName(),applicationInfo.getAppId());
            throw new RuntimeException("端口已被占用");
        }

        zkClient.subscribeDataChanges(nodePath, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                logger.info("节点数据有变化，{}","datapath="+dataPath+"\tdata="+String.valueOf(data));
                List<ApplicationInfo> applicationInfos = providerMap.get(path);
                dataPath = dataPath.substring(dataPath.lastIndexOf("/")+1);
                for (int index=0;index < applicationInfos.size();index++) {
                    if(dataPath.equals(applicationInfo.getAppId())) {
                        applicationInfos.set(index, (ApplicationInfo) data);
                    }
                }
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                logger.info("节点数据被清空标识该节点被删除");
                zkClient.delete(dataPath);
            }
        });

        logger.info("应用[{}]的实例[{}]已上线",applicationInfo.getAppName(),applicationInfo.getAppId());
        refreshInstance(path);
        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                logger.info("实例状态为[{}]",keeperState);
                if(Watcher.Event.KeeperState.Disconnected.getIntValue() == keeperState.getIntValue()) {
                    zkConnection.close();
                    zkConnection.connect(zkClient);
                }
            }

            @Override
            public void handleNewSession() throws Exception {

            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {

            }
        });
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
            List<ApplicationInfo> applicationInfos = null;
            if(CollectionUtils.isEmpty(providerMap)) {
                applicationInfos = new ArrayList<>();
            }else {
                applicationInfos = providerMap.get(path);
            }
            String serviceName = path.substring(path.lastIndexOf("/")+1);
            logger.info("service[{}] old applicationInfo's length is{}",serviceName,applicationInfos.size());
//            List<ApplicationInfo> applicationInfos = new ArrayList<>();
            applicationInfos.clear();
            for (String child : children) {
                applicationInfos.add((ApplicationInfo)zkClient.readData(path + "/" + child));
            }
            logger.info("service[{}] new applicationInfo's length is{}",serviceName,applicationInfos.size());
            providerMap.put(path,applicationInfos);
        }
    }


    public void writeRequestTraceLog() {
        //先写入本地的阻塞队列，然后再消费
        ThreadLocalProcessorTracer tracer = ThreadLocalProcessorTracer.get();
        Span spanInfo = tracer.getSpanInfo();
        logger.info("将traceLog消息放到阻塞队列，等待消费");
        boolean offer = spanBlockingQueue.offer(spanInfo);
        if(!offer) {
            logger.info("请求跟踪日志已满");
        }

    }
}
