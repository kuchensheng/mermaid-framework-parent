package com.mermaid.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2018/8/29 11:22
 */
@Configuration
public class AnnotaionProxyFactoryBean implements EnvironmentAware {

    private static String BASE_PACKAGES="com,cn,org,gov";

    private static final Logger logger = LoggerFactory.getLogger(AnnotaionProxyFactoryBean.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        setComonetScanValue();
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
            Class<?> className = Class.forName("com.mermaid.framework.MermaidApplicationEntry");
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
}
