package com.mermaid.framework.core.cloud;

import com.mermaid.framework.core.application.ApplicationInfo;
import com.mermaid.framework.core.cloud.annotation.CloudClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/3 21:51
 * version 1.0
 */
public class CloudClientScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger logger = LoggerFactory.getLogger(CloudClientScanner.class);

    private Environment environment;

    private ApplicationInfo applicationInfo;

    private PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

    public CloudClientScanner(ApplicationInfo applicationInfo,BeanDefinitionRegistry registry, Environment environment) {
        super(registry, false);
        this.environment = environment;
        this.pathMatchingResourcePatternResolver = pathMatchingResourcePatternResolver;
        this.applicationInfo = applicationInfo;
        this.setBeanNameGenerator(new CloudClientProxyBeanNameGenerator());
        this.initFilters();
    }

    private void initFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(CloudClient.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
        if(isCandidate && this.getRegistry() instanceof ConfigurableListableBeanFactory) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                String[] beanNames = ((ConfigurableListableBeanFactory) this.getRegistry()).getBeanNamesForType(clazz);
                if(beanNames.length > 0) {
                    logger.info("远程接口["+beanDefinition.getBeanClassName() +"]定义了本地实现，不再创建远程代理，使用本地实现");
                    isCandidate = false;
                }
            } catch (ClassNotFoundException e) {
                isCandidate = false;
            }
        }
        return isCandidate;
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        if(beanName.equals("cloudProxy_"+ this.applicationInfo.getAppName())) {
            return false;
        }
        return true;
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        String beanName = definitionHolder.getBeanName();
        String className = definitionHolder.getBeanDefinition().getBeanClassName();
        GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) definitionHolder.getBeanDefinition();
        genericBeanDefinition.setBeanClass(CloudClientProxyFactoryBean.class);
    }

    private static class CloudClientProxyBeanNameGenerator implements BeanNameGenerator {
        @Override
        public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
            String beanName = null;
            try {
                Class<?> proxyInterface = Class.forName(definition.getBeanClassName());
                CloudClient cloudClientAnnotation = AnnotationUtils.findAnnotation(proxyInterface, CloudClient.class);
                beanName = proxyInterface.getName() + "_" + cloudClientAnnotation.value();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getCause());
            }
            return "cloudProxy_"+beanName;
        }
    }
}
