package com.mermaid.framework.core.changer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ClassName:MermaidSpringValueProcessor
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  14:25
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 14:25   kuchensheng    1.0
 */
public class MermaidSpringValueProcessor extends MermaidProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(MermaidSpringValueProcessor.class);

    @Override
    protected void processField(Object bean, String beanName, Field field) {

    }

    @Override
    protected void processMethod(Object bean, String beanName, Method method) {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
