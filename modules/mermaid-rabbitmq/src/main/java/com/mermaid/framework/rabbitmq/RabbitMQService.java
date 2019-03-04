package com.mermaid.framework.rabbitmq;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-04 21:58
 * 描述：消息服务接口
 */
public interface RabbitMQService {

    void send(String queueName,byte[] data);

    void send(RabbitMQMessageTarget target,byte[] data);

    void listen(RabbitMessageListener listener);

    void listen(RabbitMessageListener listener,Integer concurrentConsumers);
}
