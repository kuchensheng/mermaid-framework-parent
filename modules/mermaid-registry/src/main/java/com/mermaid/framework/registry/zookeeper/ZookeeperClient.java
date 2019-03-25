package com.mermaid.framework.registry.zookeeper;

import java.util.List;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-22 11:12
 * 描述：Zookeeper客户端行为规范
 */
public interface ZookeeperClient {

    /**
     * 创建节点，默认创建父路径
     * @param path 路径
     * @param ephemeral 是否是临时节点
     */
    void create(String path,boolean ephemeral);

    /**
     * 创建节点，默认创建父路径
     * @param path 路径
     * @param data 数据对象
     * @param ephemeral 是否是临时节点
     */
    void create(String path,Object data,boolean ephemeral);

    /**
     * 删除节点
     * @param path
     */
    void delete(String path);

    List<String> getChildren(String path);

    List<String> addChildListener(String path, IChildListener childListener);

    void removeChildListener(String path,IChildListener listener);

    void addStateListener(StateListener listener);

    void removeStateListener(StateListener listener);

    void close();
}
