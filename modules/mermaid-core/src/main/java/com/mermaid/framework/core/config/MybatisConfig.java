package com.mermaid.framework.core.config;

import com.github.pagehelper.PageHelper;
import com.mermaid.framework.util.MavenUtil;
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

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() throws ClassNotFoundException {
        String groupId = null;
        try {
            groupId = MavenUtil.getTagContent("groupId");
        } catch (Exception e) {
            throw new RuntimeException("获取group ID异常请检查",e);
        }
        String mybatisMapperScanBasePackage = environment.getProperty("mermaid.framework.mybatis.mapper.scan.basePackages",groupId);
        if(!mybatisMapperScanBasePackage.contains(groupId)) {
            mybatisMapperScanBasePackage = mybatisMapperScanBasePackage.endsWith(",") ? mybatisMapperScanBasePackage + groupId : mybatisMapperScanBasePackage+","+groupId;
        }
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisMapperScanBasePackage);
        mapperScannerConfigurer.setAnnotationClass(Class.forName("org.apache.ibatis.annotations.Mapper").asSubclass(java.lang.annotation.Annotation.class));

        return mapperScannerConfigurer;
    }

    @Bean
    public PageHelper pageHelper() {
        String helperDialect = environment.getProperty("mermaid.pagehelper.helperDialect","mysql");
        String offsetAsPageNum = environment.getProperty("mermaid.pagehelper.offsetAsPageNum","true");
        String rowBoundsWithCount = environment.getProperty("mermaid.pagehelper.rowBoundsWithCount","true");
        String reasonable = environment.getProperty("mermaid.pagehelper.reasonable","true");
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
