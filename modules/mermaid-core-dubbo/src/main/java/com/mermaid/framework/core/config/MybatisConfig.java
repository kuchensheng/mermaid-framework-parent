package com.mermaid.framework.core.config;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

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

    private static final String DEFAULT_BASEPACKAGES = "com.mermaid";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() throws ClassNotFoundException {
        String mybatisMapperScanBasePackage = environment.getProperty("mybatis.mapper.scan.basePackages");
        mybatisMapperScanBasePackage = !StringUtils.isEmpty(mybatisMapperScanBasePackage) && !DEFAULT_BASEPACKAGES.equals(mybatisMapperScanBasePackage) ? mybatisMapperScanBasePackage +","+DEFAULT_BASEPACKAGES : DEFAULT_BASEPACKAGES;
        String mybatisMapperAnnotation = environment.getProperty("mybatis.mapper.scan.annotation","org.apache.ibatis.annotations.Mapper");

        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisMapperScanBasePackage);
        if(!StringUtils.hasText(mybatisMapperAnnotation)) {
            logger.info("未读取到mybatis.mapper.scan.annotation，设置默认值={}","org.apache.ibatis.annotations.Mapper");
        }
        mapperScannerConfigurer.setAnnotationClass(Class.forName(mybatisMapperAnnotation).asSubclass(java.lang.annotation.Annotation.class));

        return mapperScannerConfigurer;
    }

    @Bean
    public PageHelper pageHelper() {
        String helperDialect = environment.getProperty("pagehelper.helperDialect","mysql");
        String offsetAsPageNum = environment.getProperty("pagehelper.offsetAsPageNum","true");
        String rowBoundsWithCount = environment.getProperty("pagehelper.rowBoundsWithCount","true");
        String reasonable = environment.getProperty("pagehelper.reasonable","true");
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum",offsetAsPageNum);
        properties.setProperty("rowBoundsWithCount",rowBoundsWithCount);
        properties.setProperty("reasonable",reasonable);
        properties.setProperty("dialect",helperDialect);
        properties.setProperty("returnPageInfo","check");
        properties.setProperty("params","count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
