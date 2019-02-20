package com.mermaid.framework.core.cloud.module;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * Desription:
 * 消费者信息
 * @author:Hui CreateDate:2018/8/19 23:52
 * version 1.0
 */
@Data
public class InvokerService {

    private Class<?> serviceIntf;

    private Object serviceObject;

    private Method serviceMethod;

    private String invokerIp;

    private Integer invokerPort;

    private long timeout;

    private String remoteAppKey;

    private String groupName = "default";
}
