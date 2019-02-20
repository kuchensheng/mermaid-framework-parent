package com.mermaid.framework.core.application;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/20 23:05
 * version 1.0
 */
public interface ApplicationDescriptor {
    /**
     * 获取描述的应用名称
     */
    String getAppName();

    /**
     * 获取描述的应用ID
     * @return
     */
    String getAppId();
}
