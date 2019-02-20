package com.mermaid.framework.core.config;

import com.mermaid.framework.core.mvc.interceptor.MermaidOptionsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/25 0:29
 * version 1.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Override
//    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//        exceptionResolvers.add(new MermaidExcepitonResolver());
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MermaidOptionsInterceptor()).addPathPatterns("/**");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("*","OPTIONS","PUT","DELETE","GET","POST")
//                .allowedHeaders("Content-Type, x-requested-with, X-Custom-Header, Authorization")
//                .maxAge(3600);
//    }
}
