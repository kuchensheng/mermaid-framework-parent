package com.mermaid.framework.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/25 16:16
 * version 1.0
 */
@Component
public class ServerPortConfig implements WebServerFactoryCustomizer<TomcatReactiveWebServerFactory> {

    @Autowired
    private Environment env;

    @Override
    public void customize(TomcatReactiveWebServerFactory factory) {

    }
}
