package com.mermaid.framework.registry;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-25 21:37
 * 描述：Zookeeper状态变更监听器
 */
public interface IStateListener {

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void stateChanged(int connected);
}
