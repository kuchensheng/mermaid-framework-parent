package com.mermaid.framework.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 9:40
 */
public class ZKClientWrapper {
    private Logger logger = LoggerFactory.getLogger(ZKClientWrapper.class);
    private long connectionTimeout;
    private long sessionTimeout;
    private ZkClient zkClient;

}
