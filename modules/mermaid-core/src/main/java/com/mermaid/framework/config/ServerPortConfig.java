package com.mermaid.framework.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/25 16:16
 * version 1.0
 */
@Component
public class ServerPortConfig implements EmbeddedServletContainerCustomizer {

    @Autowired
    private Environment env;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        String port = env.getProperty("spring.application.index");
        if (StringUtils.hasText(port)) {
            container.setPort(Integer.valueOf(port));
        }
    }
}
