package com.mermaid.framework.rabbitmq;

import com.codahale.metrics.Timer;
import com.mermaid.framework.rabbitmq.monitor.RabbitMQMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 8:43
 */
public class SimpleRabbitMQServiceImpl implements RabbitMQService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRabbitMQServiceImpl.class);

    private ConnectionFactory rabbitMqConnectionFactory;

    private RabbitTemplate rabbitTemplate;

    private RabbitAdmin rabbitAdmin;

    private MessageConverter messageConverter;

    private RabbitMQMetrics rabbitMQMetrics;

    private Set<String> declaredExchangeAndQueues = new HashSet<String>();

    private Set<String> declaredQueues = new HashSet<String>();

    public SimpleRabbitMQServiceImpl(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitMqConnectionFactory = connectionFactory;
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitMQMetrics = new RabbitMQMetrics(this.getClass().getSimpleName());
    }

    @Override
    public void send(String queueName, Object data) {
        send(queueName,queueName, ExchangeTypes.DIRECT,data,new String[]{queueName});
    }

    @Override
    public void send(RabbitMQMessageTarget target, Object data) {
        send(target.getExchangeName(),target.getRoutingKey(),target.getExchangeTypes(),data,target.getQueueNames());
    }

    private void send(String exchangeName, String routingKey, String exchangeType, Object data, String[] queueNames) {
        Timer.Context ctx = rabbitMQMetrics.startSendTiming();
        if(StringUtils.isEmpty(exchangeName)) {
            throw new IllegalArgumentException("exchange or routingKey must not null");
        }
        this.declareExchangeAndQueue(exchangeName,routingKey,exchangeType,queueNames);

        try {
            rabbitTemplate.convertAndSend(exchangeName,routingKey,data);
            rabbitMQMetrics.incSendSuccessCount();
        } catch (AmqpException e) {
            logger.error("RabbitMQ send exception:"+e.getMessage(),e);
            rabbitMQMetrics.incSendFailCount();
            throw e;
        } finally {
            ctx.stop();
        }
    }

    /**
     * 绑定exchange和queue
     * @param exchangeName
     * @param routingKey
     * @param exchangeType
     * @param queueNames
     */
    private void declareExchangeAndQueue(String exchangeName, String routingKey, String exchangeType, String[] queueNames) {
        if(queueNames != null && queueNames.length > 0 ) {
            for (String queueName : queueNames) {
                String exchangeAndQueue = exchangeName + "|"+queueName;
                if(!declaredExchangeAndQueues.contains(exchangeAndQueue)) {
                    Queue queue = new Queue(queueName);
                    queue.setAdminsThatShouldDeclare(rabbitAdmin);
                    rabbitAdmin.declareQueue(queue);
                    switch (exchangeType) {
                        case ExchangeTypes.TOPIC:
                            TopicExchange topicExchange = new TopicExchange(exchangeName);
                            rabbitAdmin.declareExchange(topicExchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));
                            break;
                        case ExchangeTypes.DIRECT:
                            DirectExchange directExchange = new DirectExchange(exchangeName);
                            rabbitAdmin.declareExchange(directExchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(routingKey));
                            break;
                        default:
                            FanoutExchange fanoutExchange = new FanoutExchange(exchangeName);
                            rabbitAdmin.declareExchange(fanoutExchange);
                            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
                            break;
                    }
                    declaredExchangeAndQueues.add(exchangeAndQueue);
                }
            }
        }
    }

    @Override
    public void listen(RabbitMessageListener listener) {
        int concurrent = 1;
        if(listener instanceof ConcurrentRabbitMessageListener) {
            concurrent = ((ConcurrentRabbitMessageListener) listener).getConcurrent();
        }
        listen(listener,concurrent);
    }

    @Override
    public void listen(final RabbitMessageListener listener, Integer concurrentConsumers) {
        String targetQueue = listener.getTargetQueueName();
        this.ensureQueueDeclared(targetQueue);

        //注册监听接口
        MessageListenerAdapter adapter = new MessageListenerAdapter(new Object(){
            public void handleMessage(Object message) {
                Timer.Context ctx = rabbitMQMetrics.startConsumeTiming();
                try {
                    listener.handleMessage(message);
                } catch (Exception e) {
                    logger.error("RabbitMQ listener handle method exception"+e.getMessage(),e);
                } finally {
                    ctx.stop();
                }
            }
        });
        if(this.messageConverter != null) {
            adapter.setMessageConverter(this.messageConverter);
        }
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitMqConnectionFactory);
        container.setMessageListener(adapter);
        container.setQueueNames(targetQueue);
        if(concurrentConsumers != null && concurrentConsumers.intValue() > 1) {
            container.setConcurrentConsumers(concurrentConsumers);
        }
        switch (listener.getAcknowledgeMode()) {
            case RabbitMessageListener.ACK_MODE_AUTO:
                container.setAcknowledgeMode(AcknowledgeMode.AUTO);
                break;
                case RabbitMessageListener.ACK_MODE_NONE:
                    container.setAcknowledgeMode(AcknowledgeMode.NONE);
                    break;
                case RabbitMessageListener.ACK_MODE_MANUAL:
                container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
                break;
                default:
                    container.setAcknowledgeMode(AcknowledgeMode.AUTO);
                    break;
        }
        container.start();
    }

    /**
     * 声明exchange和queue
     * @param targetQueue
     */
    private void ensureQueueDeclared(String targetQueue) {
        if(!declaredQueues.contains(targetQueue)) {
            Queue queue = new Queue(targetQueue);
            queue.setAdminsThatShouldDeclare(rabbitAdmin);
            rabbitAdmin.declareQueue(queue);
            declaredQueues.add(targetQueue);
        }
    }
}
