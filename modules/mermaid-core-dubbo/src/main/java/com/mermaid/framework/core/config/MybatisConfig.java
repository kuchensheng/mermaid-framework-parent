package com.mermaid.framework.core.config;

import com.github.pagehelper.PageHelper;
import com.mermaid.framework.util.MavenUtil;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
@ConditionalOnExpression("${com.mermaid.mybatis.enable:false} == true")
public class MybatisConfig implements EnvironmentAware{
    private static final Logger logger  = LoggerFactory.getLogger(MybatisConfig.class);


    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() throws ClassNotFoundException {
        String mybatisMapperScanBasePackage = null;
        try {
            mybatisMapperScanBasePackage = "com.mermai,"+ MavenUtil.getTagContent("groupId");
        } catch (Exception e) {
            throw new RuntimeException("获取group ID信息异常",e);
        }

        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisMapperScanBasePackage);

        mapperScannerConfigurer.setAnnotationClass(Class.forName("org.apache.ibatis.annotations.Mapper").asSubclass(java.lang.annotation.Annotation.class));

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
