package com.mermaid.framework.core.apollo;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.property.AutoUpdateConfigChangeListener;
import com.mermaid.framework.registry.EventType;
import com.mermaid.framework.registry.IDataListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/18 0:20
 * version 1.0
 */
public class MermaidDataChangeListener implements IZkDataListener,EnvironmentAware {

    private Environment environment;

    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    public MermaidDataChangeListener(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        //TODO 利用apollo的自动更新原理进行数据变更
        AutoUpdateConfigChangeListener autoUpdateConfigChangeListener = new AutoUpdateConfigChangeListener(this.environment, this.configurableListableBeanFactory);
        ConfigChangeEvent changeEvent = JSONObject.parseObject(String.valueOf(data), ConfigChangeEvent.class);
        autoUpdateConfigChangeListener.onChange(changeEvent);
        Set<String> changedKeys = changeEvent.changedKeys();
        for (String key : changedKeys) {
            if(key.contains("datasource")) {
                //TODO 重启spring容器
                new MermaidApplicationContext().refresh();
            }
        }
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {

    }

    class MermaidApplicationContext extends ServletWebServerApplicationContext {

    }
}
