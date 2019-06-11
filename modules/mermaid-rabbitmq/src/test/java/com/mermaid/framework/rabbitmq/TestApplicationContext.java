package com.mermaid.framework.rabbitmq;

import com.mermaid.framework.rabbitmq.support.RunTimeUtil;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 13:47
 */
@SpringBootApplication
public class TestApplicationContext {
//    public static void main(String[] args) {
//        SpringApplication.run(TestApplicationContext.class,args);
//    }
//
    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setNamesrvAddr("39.98.239.1:30001");
        clientConfig.setVipChannelEnabled(false);
        clientConfig.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
        return clientConfig;
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.resetClientConfig(clientConfig());
        producer.setProducerGroup("local_pufang_producer");
        producer.setSendMsgTimeout(5000);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }

    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer() {
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer();
        pushConsumer.resetClientConfig(clientConfig());
        pushConsumer.setConsumerGroup("local_pufang_producer");
        return pushConsumer;
    }
}
