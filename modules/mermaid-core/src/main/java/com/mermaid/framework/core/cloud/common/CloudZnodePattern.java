package com.mermaid.framework.core.cloud.common;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/21 23:59
 * version 1.0
 */
public class CloudZnodePattern {

    public static final String ZNODE_INSTANCE = "/applications";

    public static final String ZNODE_INSTANCE_GROUP = "/applications/${appName}";
    /**该节点写入的数据类型为CloudApplication, 表示云应用实例信息*/
    public static final String ZNODE_INSTANCE_INFORMATION="/applications/${appName}/${appId}";
}
