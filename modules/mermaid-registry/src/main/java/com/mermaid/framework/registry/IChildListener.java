package com.mermaid.framework.registry;

import java.util.List;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-22 11:18
 * 描述：子节点监听器
 */
public interface IChildListener {
    /**
     * 当path路径下的节点有变化时，触发此监听
     * @param path
     * @param children
     */
    void doChildChange(String path, List<String> children);
}
