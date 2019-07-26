package com.mermaid.framework.core.config;

import com.alibaba.dubbo.config.ApplicationConfig;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;

import com.mermaid.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


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
public class DubboServiceConfig implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DubboServiceConfig.class);
    @Value("${mermaid.provider.group:test}")
    private String group;

    @Value("${spring.application.name}")
    private String appliation;

    @Value("${mermaid.provider.version}")
    private String providerVersion;

    ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
            try {
                ServiceConfig serviceBean = ((AnnotationConfigServletWebServerApplicationContext) this.applicationContext).getBeanFactory().getBean(ServiceConfig.class);
                if (!StringUtils.isEmpty(appliation)) {
                    serviceBean.setApplication(new ApplicationConfig(appliation));
                }

                if (!StringUtils.isEmpty(group)) {
                    serviceBean.setGroup(group);
                }
                if (!StringUtils.isEmpty(providerVersion)) {
                    serviceBean.setVersion(providerVersion);
                }
            } catch (Exception e) {
                logger.warn("dubbo service config not found,may be it is not a dubbo provider",e.getMessage());
            }

            try {
                ReferenceConfig referenceConfig = ((AnnotationConfigServletWebServerApplicationContext) this.applicationContext).getBeanFactory().getBean(ReferenceConfig.class);
                referenceConfig.setVersion("*");
            } catch (Exception e) {
                logger.warn("dubbo reference config not found,may be it is not a dubbo consumer",e.getMessage());
            }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;


    }


}
