package com.mermaid.framework.core.config.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取本地文件配置
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 10:24
 */
public class LocalFileConfigFactory extends AbstractConfigFactory {
    private Logger logger = LoggerFactory.getLogger(LocalFileConfigFactory.class);

    private static final String CLASSPATH_CONFIG_RESOURCE_NAME="application.properties";

    public LocalFileConfigFactory(String configFile) {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            if(StringUtils.hasText(configFile)) {
                File localConfigFile = new File(configFile);
                logger.info("***NOTE***:正在使用本地配置文件 -> {}",localConfigFile.getCanonicalPath());
                if(!localConfigFile.exists()) {
                    throw new RuntimeException("找不到本地配置文件 ->" + localConfigFile.getCanonicalPath());
                }
                is = new FileInputStream(localConfigFile);
            }else {
                ClassPathResource classPathResource = new ClassPathResource(CLASSPATH_CONFIG_RESOURCE_NAME);
                is = classPathResource.getInputStream();
            }
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        addConfigs(properties);
    }

    public void connect2Cloud(Properties properties) {
        logger.info("正在以应用[{}]的身份[{}]连接到云平台[{}]",properties.getProperty("spring.application.name"),properties.get("mermaid.cloud.ticket"),properties.getProperty("mermaid.cloud.url"));
        //fixme:从微服务平台获取配置后，设置到properties中去。

    }

    private String getMermaidFrameworkVersion() throws IOException {
        String version = "UNKNOWN";
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//        Resource[] resource = pathMatchingResourcePatternResolver.getResources("classpath*:META-INFO/mermaid-framework-core.properties");
        Resource resource = pathMatchingResourcePatternResolver.getResource("classpath*:META-INF/mermaid-framework-core.properties");
        if(null != resource ) {
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            version = properties.getProperty("mermaid.framework.version");
        }
        return version;

    }
}
