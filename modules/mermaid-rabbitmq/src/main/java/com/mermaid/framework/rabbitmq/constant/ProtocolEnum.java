package com.mermaid.framework.rabbitmq.constant;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/29 17:38 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public enum ProtocolEnum {
    RABBITMQ("rabbitmq"),
    ROCKETMQ("rocketmq"),
    ACTIVEMQ("activemq"),
    KAFKA("kafka")
    ;
    private String value;

    ProtocolEnum(String value) {
        this.value = value;
    }
}
