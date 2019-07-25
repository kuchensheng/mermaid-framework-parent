package com.mermaid.framework.core.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.mermaid.framework.util.StringUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName:DubboServerConfig
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  15:09
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 15:09   kuchensheng    1.0
 */
@Component
public class DubboServiceConfig {

    @Value("${mermaid.provider.group:test}")
    private String group;

    @Value("${spring.application.name}")
    private String appliation;

    @Resource
    private ServiceConfig serviceBean;

    @Bean
    public ServiceConfig serviceConfig() {
        if (!StringUtils.isEmpty(appliation)) {
            serviceBean.setApplication(new ApplicationConfig(appliation));
        }

        if (!StringUtils.isEmpty(group)) {
            serviceBean.setGroup(group);
        }
        return serviceBean;
    }


}
