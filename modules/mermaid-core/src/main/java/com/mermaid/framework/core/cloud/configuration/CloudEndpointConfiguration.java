package com.mermaid.framework.core.cloud.configuration;

import com.mermaid.framework.core.cloud.CloudEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/26 16:19
 */
@Configuration
@ConditionalOnExpression("${mermaid.framework.cloud.enable:false}==true")
public class CloudEndpointConfiguration implements EnvironmentAware{

    private String protocol;

    @Override
    public void setEnvironment(Environment environment) {
        this.protocol = environment.getProperty("mermaid.framework.protocol","eureka");
        if("zookeeper".equals(protocol)) {
            initZookeeper();
        }
    }

    private void initZookeeper() {
        new ZookeeperInitor();
    }

    private class ZookeeperInitor {

    }

    @Bean
    @ConditionalOnBean(ZookeeperInitor.class)
    public CloudEndpoint cloudEndpoint() {
        return new CloudEndpoint();
    }
}
