package com.mermaid.framework.rabbitmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/29 17:35 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
@Service
public class SimpleRocketMQServiceImpl implements RocketMQService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRocketMQServiceImpl.class);

    @Autowired
    private DefaultMQPushConsumer mqPushConsumer;

    @Autowired
    private DefaultMQProducer mqProducer;

    @Value("${mermaid.framework.rocketmq.producer.timeout:3000}")
    private long timeout;

    @Override
    public SendResult sendMessage(String topic, byte[] data) {
        return sendMessage(new Message(topic,data));
    }

    @Override
    public SendResult sendMessage(Message message) {
        return sendMessage(message,timeout);
    }

    @Override
    public SendResult sendMessage(Message message, long timeout) {
        try {
            SendResult send = mqProducer.send(message,timeout);
            logger.debug("send rocketmq message,messageId:{}", message.getBuyerId());
            return send;
        } catch (Exception e) {
            logger.error("send rocketmq message error",e);
        }
        return null;
    }

    @Override
    public void listen(final RocketMessageListener listener) {
        //注册监听接口
        try {
            mqPushConsumer.registerMessageListener(listener);
            mqPushConsumer.start();
            logger.info("开启监听，topic={}",listener.getListenerTopic());
        } catch (MQClientException e) {
            logger.error("监听失败",e);
        }

    }
}
