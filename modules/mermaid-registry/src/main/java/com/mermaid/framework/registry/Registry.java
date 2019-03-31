package com.mermaid.framework.registry;

import java.util.List;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-08 11:08
 * 描述：注册中心Facade
 */
public interface Registry {

    /**
     * 创建节点
     * @param path 路径
     * @param ephemeral 标识是否是临时节点
     */
    void create(String path,boolean ephemeral);

    /**
     * 创建节点，并写入数据
     * @param path 路径
     * @param ephemeral 标识是否是临时节点
     * @param data 数据信息
     */
    void create(String path,boolean ephemeral,Object data);

    /**
     * 获取子节点信息
     * @param path 子节点路径
     * @return 子节点信息列表
     */
    List<String> getChildren(String path);

    /**
     * 删除节点信息
     * @param path 节点路径
     */
    void delete(String path);

    /**
     * 获取节点数据信息
     * @param path 节点信息
     * @param <T> 返回数据类型
     * @return
     */
    <T> T getData(String path);

    /**
     * 是否已关闭
     */
    void close();

    /**
     * 添加状态监听
     * @param stateListener
     */
    void addStateListener(IStateListener stateListener);

    /**
     * 移除状态监听
     * @param stateListener
     */
    void removeStateListener(IStateListener stateListener);

    /**
     * 添加子节点信息变化监听器
     * @param path
     * @param childListener
     */
    void addChildListener(String path,IChildListener childListener);

    void removeChildListener(String path,IChildListener childListener);

    void addDataListener(String path,IDataListener dataListener);

    boolean isConnected();
}
