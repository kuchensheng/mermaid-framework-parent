package com.mermaid.framework.core.cloud.constant;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/22 23:08
 * version 1.0
 */
public interface CloudZonePattern {

    /**
     * 该节点下的都是服务实例实时状态数据
     * */
    String ZNODE_REALMS = "/realms";

    /**
     * 该节点下的都是服务持久数据
     * */
    String ZNODE_APPLICATION = "/applications";
}
