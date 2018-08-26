package com.mermaid.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com"})
@ComponentScan(basePackages = {"com"})
@Slf4j
public class MermaidApplicationEntry {

    private static final String CLASSPATH_CONFIG_RESOURCE_NAME = "application.properties";

    private static final String CLASSPATH_CONFIG_MODEL_NAME="classpath*:META-INF/mermaid-framework*.properties";

    public static void main(String[] args) {

        Properties properties = detectApplicationProperties(CLASSPATH_CONFIG_MODEL_NAME);
        SpringApplication springApplication = new SpringApplicationBuilder(MermaidApplicationEntry.class).web(true).build();
        springApplication.setDefaultProperties(properties);
        printConfigInfo(properties);
        springApplication.run(args);
    }

    private static void printConfigInfo(Properties properties) {
        log.info("==============实例配置=======================");
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Object,Object> entry : properties.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("\n");
        }
        sb.deleteCharAt(sb.lastIndexOf("\n"));
        log.info("\n"+sb.toString());
    }

    private static Properties detectApplicationProperties(String classpathConfigModelName) {
        try {
            Properties properties = new Properties();
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = pathMatchingResourcePatternResolver.getResources(CLASSPATH_CONFIG_MODEL_NAME);
            Resource applicationResource = pathMatchingResourcePatternResolver.getResource(CLASSPATH_CONFIG_RESOURCE_NAME);
            if(null != resources && resources.length > 0) {
                for (Resource resource : resources) {
                    log.info("读取本地配置文件[{}]",resource.getFilename());
                    Properties moduleResources = PropertiesLoaderUtils.loadProperties(resource);
                    mergeProperties(properties,moduleResources);
                }
            }
            Properties applicationProperties = PropertiesLoaderUtils.loadProperties(applicationResource);
            log.info("读取application.properties=[{}]",applicationResource.getFilename());
            mergeProperties(properties,applicationProperties);
            //TODO 后续考虑从远程（配置中心）读取properties，并且以之为核心进行配置合并
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("File not found",e);
        }
    }

    private static void mergeProperties(Properties properties, Properties moduleResources) {
//        log.info("merge configuration，if find same key,value will be set last properites file's value");
        for(Map.Entry<Object,Object> entry : moduleResources.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = StringUtils.isEmpty(entry.getValue()) ? "" : String.valueOf(entry.getValue());
            if(properties.contains(key)) {
                log.info("find key={}，overwrite old value,new value={}",key,value);
                properties.setProperty(key,value);
            }else {
                properties.put(key,value);
            }
        }
    }
}
