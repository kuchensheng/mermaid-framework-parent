package com.mermaid.framework.rabbitmq.configuration;

import com.mermaid.framework.rabbitmq.RabbitMQService;
import com.mermaid.framework.rabbitmq.RabbitMessageListener;
import com.mermaid.framework.rabbitmq.SimpleRabbitMQServiceImpl;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/5 8:44
 */
@Configuration
//@ConditionalOnExpression("${mermaid.framework.mq.protocol:rabbitmq}=='rabbitmq'")
public class RabbitMQAutoConfiguration extends AbstractMQAutoConfiguration{

    @Value("${mermaid.framework.rabbitmq.host:127.0.0.1}")
    private String host;

    @Value("${mermaid.framework.rabbitmq.port:5672}")
    private String port;
    @Value("${mermaid.framework.rabbitmq.userName:guest}")
    private String userName;
    @Value("${mermaid.framework.rabbitmq.password:guest}")
    private String password;
    @Value("${mermaid.framework.rabbitmq.report.interval:30}")
    private int reportInterval;
    @Value("${mermaid.framework.rabbitmq.addresses:}")
    private String addresses;
    @Value("${mermaid.framework.rabbitmq.vhost:/}")
    private String vhost;
    @Value("${mermaid.framework.rabbitmq.maxChannels:25}")
    private int maxChannels;
    @Value("${mermaid.framework.rabbitmq.requestedHeardBeat:60}")
    private int requestedHeardBeant;

    @Value("${mermaid.framework.rabbitmq.connectionTimeout:60000}")
    private int connectionTimeout;

    @Value("${mermaid.framework.rabbitmq.automaticRecoveryEnabled:false}")
    private boolean automaticRecoveryEnabled;

    @Value("${mermaid.framework.rabbitmq.topologyRecoveryEnabled:true}")
    private boolean topologyRecoveryEnabled;

    @Value("{mermaid.framework.mq.protocol:rabbitmq}")
    private String rabbitmq;


    @Bean
    public ConnectionFactory rabbitMqConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(Integer.parseInt(port));
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(vhost);
        factory.setAddresses(addresses);
        factory.setRequestedHeartBeat(requestedHeardBeant);
        factory.setConnectionTimeout(connectionTimeout);
        factory.setChannelCacheSize(maxChannels);
        factory.getRabbitConnectionFactory().setTopologyRecoveryEnabled(topologyRecoveryEnabled);
        factory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(automaticRecoveryEnabled);
        return factory;
    }

    @Bean
    public RabbitMQService rabbitMQService(ConnectionFactory connectionFactory) {
        return new SimpleRabbitMQServiceImpl(connectionFactory,new RabbitTemplate(connectionFactory),new RabbitAdmin(connectionFactory));
    }
}
