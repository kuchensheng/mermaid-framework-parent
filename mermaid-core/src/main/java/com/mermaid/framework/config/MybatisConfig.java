package com.mermaid.framework.config;

import com.mermaid.framework.constant.ModuleConstants;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/26 23:19
 * version 1.0
 */
@Configuration
public class MybatisConfig implements EnvironmentAware{
    private static final Logger logger  = LoggerFactory.getLogger(MybatisConfig.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() throws ClassNotFoundException {
        String mybatisMapperScanBasePackage = environment.getProperty("mermaid.framework.mybatis.mapper.scan.basePackages");
        String mybatisMapperAnnotation = environment.getProperty("mermaid.framework.mybatis.mapper.scan.annotation");
        if(!StringUtils.hasText(mybatisMapperScanBasePackage)) {
            //hack code to avoid mapper scan error
            mybatisMapperScanBasePackage = "com.mermaid";
        }
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisMapperScanBasePackage);
        if(StringUtils.hasText(mybatisMapperAnnotation)) {
            mapperScannerConfigurer.setAnnotationClass(Class.forName(mybatisMapperAnnotation).asSubclass(java.lang.annotation.Annotation.class));
        }
        return mapperScannerConfigurer;
    }
}
