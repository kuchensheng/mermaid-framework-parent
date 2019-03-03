package com.mermaid.framework.core.cloud;

import com.mermaid.framework.core.application.ApplicationInfo;
import feign.Contract;
import feign.codec.Encoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/3/3 22:20
 * version 1.0
 */
public class CloudClientProxyFactoryBean implements FactoryBean<Object>,ApplicationContextAware{

    private String cloudServiceClassName;

    private String cloudServiceName;

    private String cloudServiceVersion;

    private Class cloudServiceClientClazz;

    private String cloudServiceUrl;

    private ApplicationInfo applicationInfo;

    private ApplicationContext applicationContext;

    private Encoder feignEncoder;

    private Contract feignContract;


    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
