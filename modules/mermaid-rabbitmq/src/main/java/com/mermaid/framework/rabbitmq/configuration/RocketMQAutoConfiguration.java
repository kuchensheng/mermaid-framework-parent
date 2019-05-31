package com.mermaid.framework.rabbitmq.configuration;

import com.mermaid.framework.rabbitmq.support.RunTimeUtil;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/5/11 21:46
 * version 1.0
 */
@Configuration
//@ConditionalOnExpression("${mermaid.framework.mq.protocol:rabbitmq}=='rocketmq'")
public class RocketMQAutoConfiguration extends AbstractMQAutoConfiguration{

    @Value("${mermaid.framework.rocketmq.namesrvAddr:39.98.239.1:30001}")
    private String namesrvAddr;

    @Value("${mermaid.framework.rocketmq.producer.producerGroup:Producer}")
    private String producerGroup;

    @Value("${mermaid.framework.rocketmq.producer.pollNameServerinteval:30000}")
    private int pollNameServerinteval;

    @Value("${mermaid.framework.rocketmq.producer.heartbeatBrokerInterval:30000}")
    private int heartbeatBrokerInterval;

    @Value("${mermaid.framework.rocketmq.producer.persistConsumerOffsetInterval:5000}")
    private int persistConsumerOffsetInterval;

    @Value("${mermaid.framework.rocketmq.producer.vipChannelEnabled:false}")
    private boolean vipChannelEnabled;

    @Value("${mermaid.framework.rocketmq.producer.createTopicKey:DEFAULT}")
    private String createTopicKey;

    @Value("${mermaid.framework.rocketmq.producer.topicQueueNums:4}")
    private int topicQueueNums;

    @Value("${mermaid.framework.rocketmq.producer.sendMsgTimeout:3000}")
    private int sendMsgTimeout;

    @Value("${mermaid.framework.rocketmq.producer.compressMsgBodyOverHowmuch:4096}")
    private int compressMsgBodyOverHowmuch;

    @Value("${mermaid.framework.rocketmq.producer.retryTimesWhenSendFailed:2}")
    private int retryTimesWhenSendFailed;

    @Value("${mermaid.framework.rocketmq.producer.retryTimesWhenSendAsyncFailed:2}")
    private int retryTimesWhenSendAsyncFailed;

    @Value("${mermaid.framework.rocketmq.producer.retryAnotherBrokerWhenNotStoreOK:false}")
    private boolean retryAnotherBrokerWhenNotStoreOK;

    @Value("${mermaid.framework.rocketmq.producer.maxMessageSize:4194304}")
    private int maxMessageSize;

    @Value("${mermaid.framework.rocketmq.consumer.PushConsumer:PushConsumer}")
    private String pushConsumer;

    @Value("${mermaid.framework.rocketmq.consumer.messageModel:CLUSTERING}")
    private MessageModel messageModel;

    @Value("${mermaid.framework.rocketmq.consumer.messageModel:CONSUME_FROM_LAST_OFFSET}")
    private ConsumeFromWhere consumeFromWhere;

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setNamesrvAddr(namesrvAddr);

        clientConfig.setClientIP(RemotingUtil.getLocalAddress());

        clientConfig.setInstanceName(environment.getProperty("spring.application.name","DEFAULT"));
        clientConfig.setClientCallbackExecutorThreads(Runtime.getRuntime().availableProcessors());
        clientConfig.setPollNameServerInterval(pollNameServerinteval);
        clientConfig.setHeartbeatBrokerInterval(heartbeatBrokerInterval);
        clientConfig.setPersistConsumerOffsetInterval(persistConsumerOffsetInterval);
        clientConfig.setVipChannelEnabled(vipChannelEnabled);
        clientConfig.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
        return clientConfig;
    }

    @Bean
    @ConditionalOnBean(ClientConfig.class)
    public DefaultMQProducer defaultMQProducer() throws Exception {
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.resetClientConfig(clientConfig());
        mqProducer.setCreateTopicKey(createTopicKey);
        mqProducer.setDefaultTopicQueueNums(topicQueueNums);
        mqProducer.setSendMsgTimeout(sendMsgTimeout);
        mqProducer.setCompressMsgBodyOverHowmuch(compressMsgBodyOverHowmuch);
        mqProducer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        mqProducer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendAsyncFailed);
        mqProducer.setRetryAnotherBrokerWhenNotStoreOK(retryAnotherBrokerWhenNotStoreOK);
        mqProducer.setMaxMessageSize(maxMessageSize);
        mqProducer.start();

        return mqProducer;
    }

    @Bean
    @ConditionalOnBean(ClientConfig.class)
    public DefaultMQPushConsumer defaultMQPushConsumer() {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(pushConsumer);
//        defaultMQPushConsumer.resetClientConfig(clientConfig());
        defaultMQPushConsumer.resetClientConfig(clientConfig());
        defaultMQPushConsumer.setMessageModel(messageModel);
        defaultMQPushConsumer.setConsumeFromWhere(consumeFromWhere);
//        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                return null;
//            }
//        });
//        defaultMQPushConsumer.setConsumeTimestamp("yyyyMMddhhmmss");
//        defaultMQPushConsumer.setAllocateMessageQueueStrategy(AllocateMessageQueueStrategy);
        return defaultMQPushConsumer;
    }
}
