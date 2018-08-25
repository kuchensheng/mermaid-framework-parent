package com.mermaid.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/20 0:09
 * version 1.0
 */
public class ZkServiceConfiguration extends ZooKeeper{

    public ZkServiceConfiguration(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
        super(connectString, sessionTimeout, watcher);
    }

    public ZkServiceConfiguration(String connectString, int sessionTimeout, Watcher watcher, boolean canBeReadOnly) throws IOException {
        super(connectString, sessionTimeout, watcher, canBeReadOnly);
    }

    public ZkServiceConfiguration(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd) throws IOException {
        super(connectString, sessionTimeout, watcher, sessionId, sessionPasswd);
    }

    public ZkServiceConfiguration(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd, boolean canBeReadOnly) throws IOException {
        super(connectString, sessionTimeout, watcher, sessionId, sessionPasswd, canBeReadOnly);
    }



}
