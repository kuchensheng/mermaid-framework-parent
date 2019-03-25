package com.mermaid.framework.registry.zookeeper;

import org.I0Itec.zkclient.*;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 9:40
 */
public class ZKClientWrapper {
    private Logger logger = LoggerFactory.getLogger(ZKClientWrapper.class);
    private int connectionTimeout;
    private long sessionTimeout;
    private ZkClient zkClient;

    private CompletableFuture<ZkClient> completableFuture;

    private volatile boolean started = false;

    public ZKClientWrapper(String serverAddresses) {
        this(serverAddresses,Integer.MAX_VALUE);
    }

    public ZKClientWrapper(String serverAddresses, int connectionTimeout) {
        this(serverAddresses,connectionTimeout,Integer.MAX_VALUE);
    }

    public ZKClientWrapper(String serverAddresses,int connectionTimeout,int sessionTimeout) {
        this.sessionTimeout =sessionTimeout;
        this.connectionTimeout = connectionTimeout;
        this.completableFuture = CompletableFuture.supplyAsync(() -> new ZkClient(serverAddresses,sessionTimeout,connectionTimeout));
    }

    public void start() {
        if(started) {
            try {
                zkClient = completableFuture.get(connectionTimeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logger.error("Timeout! zookeeper server can not be connected in : " + connectionTimeout +"ms!");
                completableFuture.whenComplete(this::makeClientReady);
            }
            started =true;
        }else {
            logger.warn("Zkclient has already been started!");
        }
    }

    public void addStateListener(IZkStateListener listener) {
        completableFuture.whenComplete((value,exception) -> {
           this.makeClientReady(value,exception);
           if(null == exception) {
               this.zkClient.subscribeStateChanges(listener);
           }
        });
    }

    public List<String> subscribeChildChanges(String path, IZkChildListener listener) {
        Assert.assertNotNull(getZkClient());
        return getZkClient().subscribeChildChanges(path,listener);
    }

    public void unsubscribeChildChanges(String path, IZkChildListener listener) {
        Assert.assertNotNull(getZkClient());
        getZkClient().unsubscribeChildChanges(path,listener);
    }

    public boolean isConnected() {
        return getZkClient() != null;
    }

    public void createPersistent(String path) {
        Assert.assertNotNull(getZkClient());
        getZkClient().createPersistent(path,true);
    }

    public void createPersistent(String path,Object data) {
        Assert.assertNotNull(getZkClient());
        getZkClient().createPersistent(path,data);
    }

    public void createEphemeral(String path) {
        Assert.assertNotNull(getZkClient());
        getZkClient().createEphemeral(path);
    }

    public void createEphemeral(String path,Object data) {
        Assert.assertNotNull(getZkClient());
        getZkClient().createEphemeral(path,data);
    }

    public void delete(String path) {
        Assert.assertNotNull(getZkClient());
        getZkClient().delete(path);
    }

    public List<String> getChildren(String path) {
        Assert.assertNotNull(getZkClient());
        return getZkClient().getChildren(path);
    }

    public String getData(String path) {
        Assert.assertNotNull(getZkClient());
        return getZkClient().readData(path);
    }

    public boolean exists(String path) {
        Assert.assertNotNull(getZkClient());
        return getZkClient().exists(path);
    }

    public void close() {
        Assert.assertNotNull(getZkClient());
        getZkClient().close();
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public ZkClient getZkClient() {
        try {
            this.zkClient = completableFuture.get();
        } catch (Exception e) {
            logger.error("zookeeper server can not be connected!");
            completableFuture.whenComplete(this::makeClientReady);
        }
        return this.zkClient;
    }

    private void makeClientReady(ZkClient zkClient, Throwable throwable) {
        if(throwable != null) {
            logger.error("Got an exception when trying to create zkclient instance, can not connect to zookeeper server, please check!", throwable);
        } else {
            this.zkClient =zkClient;
        }
    }

}
