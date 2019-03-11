package com.mermaid.framework.registry.zookeeper;

import org.I0Itec.zkclient.*;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 9:40
 */
public class ZKClientWrapper {
    private Logger logger = LoggerFactory.getLogger(ZKClientWrapper.class);
    private int connectionTimeout;
    private long sessionTimeout;
    private ZkClient zkClient;
    private IZkConnection zkConnection;

    private FutureTask<ZkClient> futureTask;

    public ZKClientWrapper(final String serverAddresses, final int connectionTimeout) throws ExecutionException, InterruptedException {
        futureTask = new FutureTask<>(new Callable<ZkClient>() {
            @Override
            public ZkClient call() throws Exception {
                zkConnection = new ZkConnection(serverAddresses);
                return new ZkClient(zkConnection,connectionTimeout);
            }
        });
        zkClient = futureTask.get();
    }

    public void addStateListener(IZkStateListener listener) {
        zkClient.subscribeStateChanges(listener);
    }

    public List<String> subscribeChildChanges(String path, IZkChildListener listener) {
        Assert.assertNotNull(zkClient);
        return zkClient.subscribeChildChanges(path,listener);
    }

    public void unsubscribeChildChanges(String path, IZkChildListener listener) {
        Assert.assertNotNull(zkClient);
        zkClient.unsubscribeChildChanges(path,listener);
    }

    public boolean isConnected() {
        return zkClient != null;
    }

    public void createPersistent(String path) {
        Assert.assertNotNull(zkClient);
        zkClient.createPersistent(path,true);
    }

    public void createPersistent(String path,Object data) {
        Assert.assertNotNull(zkClient);
        zkClient.createPersistent(path,data);
    }

    public void createEphemeral(String path) {
        Assert.assertNotNull(zkClient);
        zkClient.createEphemeral(path);
    }

    public void createEphemeral(String path,Object data) {
        Assert.assertNotNull(zkClient);
        zkClient.createEphemeral(path,data);
    }

    public void delete(String path) {
        Assert.assertNotNull(zkClient);
        zkClient.delete(path);
    }

    public List<String> getChildren(String path) {
        Assert.assertNotNull(zkClient);
        return zkClient.getChildren(path);
    }

    public String getData(String path) {
        Assert.assertNotNull(zkClient);
        return zkClient.readData(path);
    }

    public boolean exists(String path) {
        Assert.assertNotNull(zkClient);
        return zkClient.exists(path);
    }

    public void close() {
        Assert.assertNotNull(zkClient);
        zkClient.close();
    }


}
