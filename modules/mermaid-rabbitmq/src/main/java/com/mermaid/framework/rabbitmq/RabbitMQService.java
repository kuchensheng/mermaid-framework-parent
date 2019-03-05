package com.mermaid.framework.rabbitmq;

/**
 * @author Chensheng.Ku
 * 创建时间 2019-03-04 21:58
 * 描述：消息服务接口
 */
public interface RabbitMQService {

    void send(String queueName,Object data);

    void send(RabbitMQMessageTarget target,Object data);

    void listen(RabbitMessageListener listener);

    void listen(RabbitMessageListener listener,Integer concurrentConsumers);
}
