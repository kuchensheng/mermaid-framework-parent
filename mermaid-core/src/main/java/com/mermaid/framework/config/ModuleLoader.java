package com.mermaid.framework.config;

import com.mermaid.framework.constant.ModuleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Desription:
 * Mermaid模块定义在META-INF/下的配置信息
 * @author:Hui CreateDate:2018/8/26 23:39
 * version 1.0
 */
public class ModuleLoader {
    private static final Logger logger = LoggerFactory.getLogger(ModuleLoader.class);

    private List<String> codePackages;

    private List<String> daoPackages;

    private List<String> mapperLocations;

    private List<String> typeAliasPackages;

    private List<String> cloudClientPackages;

    private List<String> configurationNamespaces;

    public ModuleLoader() {
        codePackages = new ArrayList<>();
        daoPackages = new ArrayList<>();
        mapperLocations = new ArrayList<>();
        typeAliasPackages = new ArrayList<>();
        cloudClientPackages = new ArrayList<>();
        configurationNamespaces = new ArrayList<>();
    }

    public Properties scanModules() throws IOException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources(ModuleConstants.MODULE_DEFINITION_RESOURCES);
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
                logger.info("正在加载Mermaid模块[" + resourcePath + "]...");
                is = resource.getInputStream();
                if (is != null) {
                    properties.load(is);
                }
                codePackages.add(properties.getProperty(ModuleConstants.CONFIG_ITEM_CODE_PACKAGE));
                daoPackages.add(properties.getProperty(ModuleConstants.CONFIG_ITEM_DAO_PACKAGE));
                cloudClientPackages.add(properties.getProperty(ModuleConstants.CONFIG_ITEM_CLOUD_CLIENT_PACKAGE));
                configurationNamespaces.add(properties.getProperty(ModuleConstants.CONFIG_ITEM_CONFIGURATION_NAMESPACES));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                is.close();
            }
        }

        Properties ret = new Properties();
        ret.setProperty(ModuleConstants.LAUNCH_ARG_CODE_PACKAGES, getModuleCodePackages());
        ret.setProperty(ModuleConstants.LAUNCH_ARG_DAO_PACKAGES, getModuleDaoPackages());
        ret.setProperty(ModuleConstants.LAUNCH_ARG_CLOUD_CLIENT_PACKAGES, getModuleCloudClientPackages());
        ret.setProperty(ModuleConstants.LAUNCH_ARG_CONFIGURATION_NAMESPACES, getModuleConfigurationNamespaces());
        logger.info("**模块代码包** => " + getModuleCodePackages());
        logger.info("**模块DAO包** => " + getModuleDaoPackages());
        logger.info("**模块云服务客户端API包** => " + getModuleCloudClientPackages());
        logger.info("**模块SQL Mapper文件(约定)** => classpath*:sql-mappers/**/*.xml, classpath*:sql-mappers-${spring.datasource.db-type}/**/*.xml");
        logger.info("**模块配置命名空间** => " + getModuleConfigurationNamespaces());
        return ret;
    }

    private String getModuleCodePackages() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < codePackages.size(); i++) {
            if (StringUtils.hasText(codePackages.get(i))) {
                if (i < codePackages.size() - 1) {
                    sb.append(codePackages.get(i)).append(",");
                } else {
                    sb.append(codePackages.get(i));
                }
            }
        }
        return sb.toString();
    }

    private String getModuleDaoPackages() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < daoPackages.size(); i++) {
            if (StringUtils.hasText(daoPackages.get(i))) {
                if (i < daoPackages.size() - 1) {
                    sb.append(daoPackages.get(i)).append(",");
                } else {
                    sb.append(daoPackages.get(i));
                }
            }
        }
        return sb.toString();
    }

    private String getModuleCloudClientPackages() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cloudClientPackages.size(); i++) {
            if (StringUtils.hasText(cloudClientPackages.get(i))) {
                if (i < cloudClientPackages.size() - 1) {
                    sb.append(cloudClientPackages.get(i)).append(",");
                } else {
                    sb.append(cloudClientPackages.get(i));
                }
            }
        }
        return sb.toString();
    }

    private String getModuleConfigurationNamespaces() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < configurationNamespaces.size(); i++) {
            if (StringUtils.hasText(configurationNamespaces.get(i))) {
                if (i < configurationNamespaces.size() - 1) {
                    sb.append(configurationNamespaces.get(i)).append(",");
                } else {
                    sb.append(configurationNamespaces.get(i));
                }
            }
        }
        return sb.toString();
    }
}
