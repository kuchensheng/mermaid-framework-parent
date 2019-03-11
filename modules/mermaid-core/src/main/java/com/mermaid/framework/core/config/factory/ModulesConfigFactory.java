package com.mermaid.framework.core.config.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 10:57
 */
public class ModulesConfigFactory extends AbstractConfigFactory {
    private Logger logger = LoggerFactory.getLogger(ModulesConfigFactory.class);

    private static final String MODULES_CONFIG_RESOURCE_NAME="classpath*:META-INF/mermaid-framework*.properties";
    public ModulesConfigFactory() {
        //TODO 此方法扫描的包太多，要注意优化
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = pathMatchingResourcePatternResolver.getResources(MODULES_CONFIG_RESOURCE_NAME);
            for (Resource resource : resources) {
                String filePath = resource.getFile().getCanonicalPath();
                logger.info("正在读取本地配置文件->{}",filePath);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                addConfigs(properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
