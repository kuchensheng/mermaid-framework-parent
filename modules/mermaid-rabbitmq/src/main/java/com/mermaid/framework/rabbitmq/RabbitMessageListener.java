package com.mermaid.framework.rabbitmq;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-04 22:01
 * 描述：
 */
public interface RabbitMessageListener {
    int ACK_MODE_AUTO = 0;

    int ACK_MODE_MANUAL = 1;

    int ACK_MODE_NONE = 2;

    /**
     * 获取监听队列名称
     * @return
     */
    String getTargetQueueName();

    /**
     * 业务处理方法
     * @param messageData
     */
    void handleMessage(Object messageData);

    int getAcknowledgeMode();
}
