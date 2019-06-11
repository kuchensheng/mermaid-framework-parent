package com.mermaid.framework.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/29 09:32 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public abstract class AbstractRocketMessageListener implements RocketMessageListener {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractRocketMessageListener.class);

    protected static final String DEFAULT_SUBEXPRESS = "*";

    private String topic;


    public AbstractRocketMessageListener(DefaultMQPushConsumer mqConsumer,String topic,String subExpression,String filterClassSource) {
        if(!StringUtils.hasText(topic)) {
            throw new RuntimeException("topic不能为空");
        }
        logger.info("rocketmq consumer[{}]开始初始化...", JSON.toJSON(mqConsumer.cloneClientConfig()));
        try {
//            mqConsumer.registerMessageListener(this);
            if(StringUtils.isEmpty(subExpression)) {
                subExpression = DEFAULT_SUBEXPRESS;
            }
            if (StringUtils.hasText(filterClassSource)) {
                mqConsumer.subscribe(topic,subExpression,filterClassSource);
            } else {
                mqConsumer.subscribe(topic,subExpression);
            }

//            mqConsumer.start();
            logger.info("初始化完成");
        } catch (MQClientException e) {
            logger.error("rocket mq 初始化失败",e);
        }
    }

    public AbstractRocketMessageListener(DefaultMQPushConsumer mqConsumer,String topic,String subExpression) {
        this(mqConsumer,topic,subExpression,null);
    }



    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(msgs)) {
            logger.warn("没有收到任何消息，直接返回{}",ConsumeConcurrentlyStatus.CONSUME_SUCCESS);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        for (MessageExt msg : msgs) {
            logger.info("receive msgId={},tags={},topic={}",msg.getMsgId(),msg.getTags(),msg.getTopic());
            if(!processMessage(msg)) {
                logger.warn("consumer fail,ask for re-consume,msgId:{}",msg.getMsgId());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * @param msg 接受到的消息体
     * @Return
     * @Decription:
     * @CreateDate: Created in 2019/5/29 17:25
     * @Author: <a href="kuchensheng@quannengzhanggui.cn">kucs</a>
     * @Modify:
     */
    protected abstract boolean processMessage(MessageExt msg);

    @Override
    public String getListenerTopic() {
        return this.getTopic();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
