package com.mermaid.framework.core.cloud.configuration;

import com.mermaid.framework.core.cloud.CloudEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/26 16:19
 */
@Configuration
@ConditionalOnExpression("'mermaid.framework.cloud.enable'==true")
public class CloudEndpointConfiguration {

    @Bean
    public CloudEndpoint cloudEndpoint() {
        return new CloudEndpoint();
    }
}
