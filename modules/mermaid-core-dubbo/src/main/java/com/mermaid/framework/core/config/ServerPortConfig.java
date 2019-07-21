package com.mermaid.framework.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/25 16:16
 * version 1.0
 */
@Component
public class ServerPortConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private Environment env;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
//        factory.setPort(env.getProperty(""));
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }
}
