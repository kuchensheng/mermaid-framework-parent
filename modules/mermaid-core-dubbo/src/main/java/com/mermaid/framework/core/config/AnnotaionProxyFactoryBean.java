package com.mermaid.framework.core.config;

import com.mermaid.framework.util.MavenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/8/29 11:22
 */
@Component
public class AnnotaionProxyFactoryBean implements ApplicationContextAware,EnvironmentAware, InitializingBean {

    private static String BASE_PACKAGES="com,cn,org,gov";

    private static final Logger logger = LoggerFactory.getLogger(AnnotaionProxyFactoryBean.class);

    private Environment environment;

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        setComonetScanValue();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        try {
            String required = MavenUtil.getTagContent("groupId");
            BASE_PACKAGES = environment.getProperty("mermaid.framework.component.scan.basePackages","com.mermiad,"+ MavenUtil.getTagContent("groupId"));
            if (!BASE_PACKAGES.contains(required)) {
                BASE_PACKAGES = BASE_PACKAGES.endsWith(",") ? BASE_PACKAGES+required : BASE_PACKAGES+","+required;
            }
            if(!BASE_PACKAGES.contains("com.mermaid")) {
                BASE_PACKAGES = BASE_PACKAGES.endsWith(",") ? BASE_PACKAGES+"com.mermaid" : BASE_PACKAGES+",com.mermaid";
            }
        } catch (Exception e) {
            throw new RuntimeException("获取key=mermaid.framework.component.scan.basePackages配置异常",e);
        }
    }

    private void setComonetScanValue() {
        String basePackage = environment.getProperty("mermaid.framework.mybatis.mapper.scan.basePackages");
        if(null != basePackage && !("").equals(basePackage)) {
            if(basePackage.indexOf(",") > -1) {
                StringBuilder sb = new StringBuilder(BASE_PACKAGES);
                for (String base : basePackage.split(",")) {
                    if(base.contains(".")) {
                        base = base.substring(0,base.indexOf("."));
                    }
                    if(BASE_PACKAGES.contains(base)) {
                        logger.info("默认值已包含{}",base);
                        continue;
                    }
                    logger.info("append {} to basepackages",base);
                    sb.append(",").append(base);
                }
            }else {
                if(basePackage.contains(".")) {
                    basePackage = basePackage.substring(0,basePackage.indexOf("."));
                }
                if(!BASE_PACKAGES.contains(basePackage)) {
                    logger.info("append {} to basepackages",basePackage);
                    BASE_PACKAGES = BASE_PACKAGES.concat(",").concat(basePackage);
                }
            }
        }
        String[] basePackages = BASE_PACKAGES.split(",");
        try {
            Class<?> className = Class.forName("com.mermaid.framework.core.MermaidFrameworkEntry");
            ComponentScan annotation = className.getAnnotation(ComponentScan.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map<String,Object> memberMap = (Map<String, Object>) memberValues.get(invocationHandler);
            logger.info("设置ComponentScan的basePackages={}",BASE_PACKAGES);
            memberMap.put("basePackages",basePackages);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
