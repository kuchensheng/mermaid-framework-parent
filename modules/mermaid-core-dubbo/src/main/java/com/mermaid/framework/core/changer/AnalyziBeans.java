package com.mermaid.framework.core.changer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:AnalyziBeans
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  11:55
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 11:55   kuchensheng    1.0
 */
//@Component
public class AnalyziBeans implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static Logger logger = LoggerFactory.getLogger(AnalyziBeans.class);
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String,Object> result = new HashMap<>();
        result.putAll(applicationContext.getBeansWithAnnotation(Configuration.class));
        result.putAll(applicationContext.getBeansWithAnnotation(Component.class));
        for (Map.Entry<String,Object> entry : result.entrySet()) {
            String key = entry.getKey();
            logger.info("bean Name = {}",key );
            Object value = entry.getValue();
            Field[] fields = value.getClass().getDeclaredFields();
            for (Field field : fields) {
                if(field.isAnnotationPresent(Value.class)) {
                    Value annotation = field.getAnnotation(Value.class);
                    if (null != annotation) {
                        String value_key = annotation.value();
                        value_key = value_key.substring(2);
                        value_key = value_key.substring(0,value_key.length()-1);
                        if(value_key.indexOf(":") > -1) {
                            value_key = value_key.substring(0,value_key.indexOf(":"));
                        }
                    }

                }
            }
        }
    }
}
