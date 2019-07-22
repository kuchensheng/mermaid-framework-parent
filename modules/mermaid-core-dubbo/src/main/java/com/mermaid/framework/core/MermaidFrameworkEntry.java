package com.mermaid.framework.core;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.config.factory.GlobalRuntimeConfigFactory;
import com.mermaid.framework.core.config.factory.LocalFileConfigFactory;
import com.mermaid.framework.core.config.factory.ModulesConfigFactory;
import com.mermaid.framework.util.IPAddressUtils;
import com.mermaid.framework.util.RuntimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@EnableDubbo
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.mermaid.framework","${mermaid.modules.basePackages:}"})
public class MermaidFrameworkEntry {
    private static final String CLASSPATH_CONFIG_RESOURCE_NAME = "application.properties";

    private static final String CLASSPATH_CONFIG_MODEL_NAME="classpath*:META-INF/mermaid-framework*.properties";

    private static Logger log = LoggerFactory.getLogger(MermaidFrameworkEntry.class);
    public static void main(String[] args) throws Exception {
        ApplicationInfo applicationInfo = buildApplicationInfo();
        buildGlobalRuntimeConfig(applicationInfo);

        GlobalRuntimeConfigFactory globalRuntimeConfigFactory = GlobalRuntimeConfigFactory.getInstance();
        applicationInfo.setRuntimeProperties(globalRuntimeConfigFactory);

        args = globalRuntimeConfigFactory.getLaunchArgs();
        printConfigInfo(globalRuntimeConfigFactory.getProperties());
        SpringApplication.run(MermaidFrameworkEntry.class,args);
        log.info("**MERMAID[{}]**应用{}:{}启动成功",applicationInfo.getAppVersion(),applicationInfo.getAppName(),applicationInfo.getAppPort());
    }

    private static void buildGlobalRuntimeConfig(ApplicationInfo applicationInfo) {
        ModulesConfigFactory modulesConfigFactory = new ModulesConfigFactory();
        GlobalRuntimeConfigFactory.getInstance().mergeConfig(modulesConfigFactory).mergeConfig(applicationInfo.getRuntimeProperties());
    }

    private static ApplicationInfo buildApplicationInfo() throws Exception {
        LocalFileConfigFactory factory = new LocalFileConfigFactory(null);
        ApplicationInfo applicationInfo = ApplicationInfo.getInstance();
        applicationInfo.setAppContextPath("/*");
        applicationInfo.setAppHost(IPAddressUtils.getLocalIP());
        applicationInfo.setAppName(factory.getValue("spring.application.name"));
        applicationInfo.setAppId(factory.getValue("server.port"));
        applicationInfo.setAppPort(Integer.parseInt(applicationInfo.getAppId()));
        applicationInfo.setLaunchTime(System.currentTimeMillis());
        applicationInfo.setPid(RuntimeUtils.getCurrentPID());
        applicationInfo.setAppVersion(factory.getValue("mermaid.framework.version"));
        applicationInfo.setRuntimeProperties(factory);
        return applicationInfo;
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
                    String absoluteFile = resource.getURL().getPath();
                    log.info("读取本地配置文件[{}]",absoluteFile);
                    InputStream inputStream = resource.getInputStream();
                    Properties moduleResources = null;
                    if(("jar").equals(resource.getURL().getProtocol())){
                        absoluteFile = absoluteFile.substring(absoluteFile.lastIndexOf("!")+1);
                        resource = new ClassPathResource(absoluteFile);
                    }
                    moduleResources = PropertiesLoaderUtils.loadProperties(resource);

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
        if(null != properties && properties.size() > 0 ) {
            for(Map.Entry<Object,Object> entry : properties.entrySet()) {
                String key = String.valueOf(entry.getKey());
                String value = StringUtils.isEmpty(entry.getValue()) ? "" : String.valueOf(entry.getValue());
                if(moduleResources.contains(key)) {
                    log.info("find key={},overwrite old value,new value={}",key,value);
                    moduleResources.setProperty(key,value);
                }
                if("spring.application.name".equals(key) || "spring.application.index".equals(key)) {
                    System.setProperty(key,value);
                }
            }
        }
        properties.putAll(moduleResources);
    }
}

