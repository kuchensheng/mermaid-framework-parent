package com.mermaid.framework.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
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
public abstract class AbstractRocketMessageListener<T> implements MessageListenerConcurrently {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractRocketMessageListener.class);

    protected static final String DEFAULT_SUBEXPRESS = "*";

    @Autowired
    private DefaultMQPushConsumer mqConsumer;

    public AbstractRocketMessageListener(String topic,String subExpression,String filterClassSource) {
        if(!StringUtils.hasText(topic)) {
            throw new RuntimeException("topic不能为空");
        }
        logger.info("rocketmq consumer[{}]开始初始化...", JSON.toJSON(mqConsumer.cloneClientConfig()));
        try {
            mqConsumer.registerMessageListener(this);
            if(StringUtils.isEmpty(subExpression)) {
                subExpression = DEFAULT_SUBEXPRESS;
            }
            if (StringUtils.hasText(filterClassSource)) {
                mqConsumer.subscribe(topic,subExpression,filterClassSource);
            } else {
                mqConsumer.subscribe(topic,subExpression);
            }

            mqConsumer.start();
            logger.info("初始化完成，并已启动,订阅主题{},subExpress={}");
        } catch (MQClientException e) {
            logger.error("rocket mq 初始化失败",e);
        }
    }

    public AbstractRocketMessageListener(String topic,String subExpression) {
        this(topic,subExpression,null);
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(msgs)) {
            logger.warn("没有收到任何消息，直接返回{}",ConsumeConcurrentlyStatus.CONSUME_SUCCESS);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        msgs.stream().forEach(msg ->{
            logger.info("receive msgId={},tags={},topic={}",msg.getMsgId(),msg.getTags(),msg.getTopic());
//            T messageBody =
        });

        return null;
    }
}
