package com.mermaid.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/25 0:29
 * version 1.0
 */
//@Configuration
//@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/js/**")
//                .addResourceLocations("classpath:/js/");
////        registry.addResourceHandler("/static/**")
////                .addResourceLocations("classpath:/static");
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
