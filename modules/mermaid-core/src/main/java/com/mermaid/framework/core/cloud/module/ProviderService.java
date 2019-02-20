package com.mermaid.framework.core.cloud.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/19 23:22
 * version 1.0
 */
@Data
public class ProviderService implements Serializable{

    /**
     * 服务接口
     */
    private Class<?> serviceIntf;

    private Object serviceObject;

    private String serverPort;

    private long timeout;

    private Object serviceProxyObject;

    private String appKey;

    private String groupName = "default";

    private int weight = 1;

    private int workderThreads = 10;

    private String serverIp;

    @JsonIgnore
    private transient Method serviceMethod;

}
