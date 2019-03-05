package com.mermaid.framework.rabbitmq;

import org.springframework.stereotype.Service;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 13:40
 */
//@Service
public class MQTestListener extends AbstractRabbitMessageListener {

    private String value;

    private String targetQueuName;

    public MQTestListener(String targetQueueName) {
        super(targetQueueName);
        this.targetQueuName = targetQueueName;
    }

    @Override
    protected void doHanleMessage(Object messageData) {
        System.out.println("listenr works:"+messageData);
        this.setValue((String)messageData);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTargetQueuName() {
        return targetQueuName;
    }

    public void setTargetQueuName(String targetQueuName) {
        this.targetQueuName = targetQueuName;
    }
}
