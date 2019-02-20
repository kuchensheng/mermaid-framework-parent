package com.mermaid.framework.core.config;

import com.mermaid.framework.core.mvc.interceptor.MermaidOptionsInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/9/19 10:28
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);
    @Bean
    public HandlerInterceptor mermaidOptionsInterceptor() {
        logger.info("创建拦截器={}",MermaidOptionsInterceptor.class.getName());
        return new MermaidOptionsInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("注册拦截器=mermaidOptionsInterceptor，pathPatterns=/**");
        registry.addInterceptor(mermaidOptionsInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
