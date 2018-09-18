package com.mermaid.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.MultipartConfigElement;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/9/18 10:11
 */
@Configuration
public class MultipartFileConfiguration implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(MultipartFileConfiguration.class);
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String maxFileSize = "1024MB";
        String maxRequestSize="1024MB";
        if(null != environment) {
            maxFileSize = environment.getProperty("mermaid.multipart.maxFileSize");
            maxRequestSize = environment.getProperty("mermaid.multipart.maxRequestSize");
            logger.info("读取配置mermaid.multipart.maxFileSize={}",maxFileSize);
            logger.info("读取配置mermaid.multipart.maxRequestSize={}",maxRequestSize);
        }
        factory.setMaxFileSize(maxFileSize);
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }
}
