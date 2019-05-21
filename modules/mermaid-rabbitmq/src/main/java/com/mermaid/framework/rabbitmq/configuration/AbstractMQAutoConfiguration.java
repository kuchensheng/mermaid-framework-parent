package com.mermaid.framework.rabbitmq.configuration;

import com.mermaid.framework.rabbitmq.RabbitMQService;
import com.mermaid.framework.rabbitmq.RabbitMessageListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.Iterator;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/5/11 22:09
 * version 1.0
 */
@Configuration
public class AbstractMQAutoConfiguration  implements ApplicationContextAware,InitializingBean,EnvironmentAware {

    @Value("${mermaid.framework.autoListen:true}")
    private boolean autoListen;

    protected ApplicationContext applicationContext;

    protected Environment environment;

    @Value("${mermaid.framework.mq.protocol:rabbitmq}")
    private String mqProtocol;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(autoListen) {
            if("rabbitmq".equals(mqProtocol)) {
                Collection<RabbitMessageListener> rabbitListeners = applicationContext.getBeansOfType(RabbitMessageListener.class).values();
                RabbitMQService rabbitMQService = applicationContext.getBean(RabbitMQService.class);
                Iterator<RabbitMessageListener> iterator = rabbitListeners.iterator();
                while (iterator.hasNext()) {
                    rabbitMQService.listen(iterator.next());
                }
            }

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
