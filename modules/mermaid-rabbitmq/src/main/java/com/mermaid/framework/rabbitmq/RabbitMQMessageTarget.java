package com.mermaid.framework.rabbitmq;

import org.springframework.amqp.core.ExchangeTypes;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 22:01
 */
public class RabbitMQMessageTarget {
    public static final RabbitMQMessageTarget createDirectTarget(String queueName) {
        return new RabbitMQMessageTarget(queueName,queueName, ExchangeTypes.DIRECT,queueName);
    }

    public static final RabbitMQMessageTarget createFanoutTarget(String exchangeName, String... queueNames) {
        return new RabbitMQMessageTarget(exchangeName, null, ExchangeTypes.FANOUT, queueNames);
    }

    public static final RabbitMQMessageTarget createTopicTarget(String exchangeName,String routingKey, String... queueNames) {
        return new RabbitMQMessageTarget(exchangeName, routingKey, ExchangeTypes.TOPIC, queueNames);
    }

    private String[] queueNames;

    private String exchangeName;

    private String routingKey;

    private String exchangeTypes;

    public RabbitMQMessageTarget() {
    }

    protected RabbitMQMessageTarget(String exchangeName, String routingKey, String exchangeTypes, String... queueNames) {
        this.queueNames = queueNames;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.exchangeTypes = exchangeTypes;
    }

    public String[] getQueueNames() {
        return queueNames;
    }

    public void setQueueNames(String[] queueNames) {
        this.queueNames = queueNames;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getExchangeTypes() {
        return exchangeTypes;
    }

    public void setExchangeTypes(String exchangeTypes) {
        this.exchangeTypes = exchangeTypes;
    }
}
