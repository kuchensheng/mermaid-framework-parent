package com.mermaid.framework.core.cloud;

import com.mermaid.framework.core.application.ApplicationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Spring容器初始化完成以后，调用BeanPostProcessor这个类，这个类实现ApplicationListener接口
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/26 15:06
 */
public class CloudEndpoint implements ResourceLoaderAware,BeanDefinitionRegistryPostProcessor,ApplicationListener<ContextRefreshedEvent>,EnvironmentAware,ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(CloudEndpoint.class);

    private static ApplicationInfo applicationInfo = ApplicationInfo.getInstance();

    private String scanPackages;

    private Environment environment;

    private ResourceLoader resourceLoader;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        if(StringUtils.hasText(this.scanPackages)) {
            logger.info("扫描包{}下的CloudClient注解",this.scanPackages);
            CloudClientScanner cloudClientScanner = new CloudClientScanner(applicationInfo,beanDefinitionRegistry,environment);
            cloudClientScanner.setResourceLoader(resourceLoader);
            cloudClientScanner.doScan(this.scanPackages.split("."));
        }

        logger.info("向配置中心上报应用配置元数据");
        applicationInfo.getCloudClient().reportApplicationConfigurationMetadata();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("项目启动，向应用中心注册资源");
        applicationInfo.getCloudClient().registerApplicationInfoResources(contextRefreshedEvent.getApplicationContext());

        applicationInfo.getCloudClient().activeApplication();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("容器初始化...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("容器被关闭,");
        applicationInfo.getCloudClient().passivateApplication();
    }
}
