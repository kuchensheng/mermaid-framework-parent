package com.mermaid.framework.config;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Properties;

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
            logger.info("未读取到mermaid.framework.mybatis.mapper.scan.basePackages的值，设置为默认值={}","com.mermaid");
            mybatisMapperScanBasePackage = "com.mermaid";
        }
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisMapperScanBasePackage);
        if(!StringUtils.hasText(mybatisMapperAnnotation)) {
            logger.info("未读取到mermaid.framework.mybatis.mapper.scan.annotation，设置默认值={}","org.apache.ibatis.annotations.Mapper");
            mybatisMapperAnnotation="org.apache.ibatis.annotations.Mapper";
        }
        mapperScannerConfigurer.setAnnotationClass(Class.forName(mybatisMapperAnnotation).asSubclass(java.lang.annotation.Annotation.class));

        return mapperScannerConfigurer;
    }

//    @Bean
//    public PageHelper pageHelper() {
////        mermaid.pagehelper.helperDialect=mysql
////        mermaid.pagehelper.offsetAsPageNum=true
////        mermaid.pagehelper.rowBoundsWithCount=true
////        mermaid.pagehelper.reasonable=true
//        String helperDialect = environment.getProperty("mermaid.pagehelper.helperDialect","mysql");
//        String offsetAsPageNum = environment.getProperty("mermaid.pagehelper.offsetAsPageNum","true");
//        String rowBoundsWithCount = environment.getProperty("mermaid.pagehelper.rowBoundsWithCount","true");
//        String reasonable = environment.getProperty("mermaid.pagehelper.reasonable","true");
//        PageHelper pageHelper = new PageHelper();
//        Properties properties = new Properties();
//        properties.setProperty("offsetAsPageNum",offsetAsPageNum);
//        properties.setProperty("rowBoundsWithCount",rowBoundsWithCount);
//        properties.setProperty("reasonable",reasonable);
//        properties.setProperty("dialect",helperDialect);
//        properties.setProperty("returnPageInfo","check");
//        properties.setProperty("params","count=countSql");
//        pageHelper.setProperties(properties);
//        return pageHelper;
//    }

}
