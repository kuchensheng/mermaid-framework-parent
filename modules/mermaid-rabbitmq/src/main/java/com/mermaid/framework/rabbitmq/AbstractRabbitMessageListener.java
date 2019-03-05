package com.mermaid.framework.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 10:24
 */
public abstract class AbstractRabbitMessageListener<T> implements ConcurrentRabbitMessageListener{
    private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitMessageListener.class);

    private String targetQueueName;

    private int acknowledgeMode = ACK_MODE_AUTO;

    public AbstractRabbitMessageListener(String targetQueueName) {
        this.targetQueueName = targetQueueName;
    }

    @Override
    public int getAcknowledgeMode() {
        return acknowledgeMode;
    }

    @Override
    public String getTargetQueueName() {
        return targetQueueName;
    }

    @Override
    public void handleMessage(Object messageData) {
        //fixme：这里是否要做链路追踪？用AOP还是直接代码写死？
        doHanleMessage((T)messageData);
    }

    protected abstract void doHanleMessage(T messageData);

    @Override
    public int getConcurrent() {
        return 1;
    }
}
