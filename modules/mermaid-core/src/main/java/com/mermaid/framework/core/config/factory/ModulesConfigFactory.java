package com.mermaid.framework.core.config.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
                Properties properties = new Properties();
                InputStream is = null;
                try {
                    String resourcePath;
                    URL path = resource.getURL();
                    if (path == null) {
                        resourcePath = resource.getFilename();
                    } else {
                        resourcePath = path.getPath();
                    }
                    logger.info("mermaid正在加载模块[{}]...",resourcePath);
                    is = resource.getInputStream();
                    if (is != null) {
                        properties.load(is);
                    }
                    addConfigs(properties);
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
