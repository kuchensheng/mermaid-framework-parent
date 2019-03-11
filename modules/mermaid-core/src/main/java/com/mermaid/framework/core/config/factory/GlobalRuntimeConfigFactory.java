package com.mermaid.framework.core.config.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 10:05
 */
public final class GlobalRuntimeConfigFactory extends AbstractConfigFactory{
    private Logger logger = LoggerFactory.getLogger(GlobalRuntimeConfigFactory.class);

    /**
     * 这里一定会用到此类，且是单例，所以采用懒汉式单例模式
     */
    private static GlobalRuntimeConfigFactory instance = new GlobalRuntimeConfigFactory();

    private GlobalRuntimeConfigFactory(){}

    public static GlobalRuntimeConfigFactory getInstance() {
        return instance;
    }

    public GlobalRuntimeConfigFactory mergeConfig(ConfigFactory configFactory) {
        if(null != configFactory) {
            addConfigs(configFactory.getProperties());
        }
        return this;
    }

    public void setProperties(String key,String newValue) {
        getProperties().setProperty(key,newValue);
    }

    public void removeProperties(String key) {
        getProperties().remove(key);
    }

    /**
     * 将配置转化为springboot command的参数形式
     * @return
     */
    public String[] getLaunchArgs() {
        Properties properties = getProperties();
        Set<Object> keySet = properties.keySet();
        List<String> argsList = new ArrayList<>();
        for (Object key: keySet) {
            String cfgValue = properties.get(key).toString();
            if(StringUtils.hasText(cfgValue)) {
                String arg = "--" + key.toString() + "=" +cfgValue;
                argsList.add(arg);
            }else {
                argsList.add("");
            }
        }
        return argsList.toArray(new String[]{});
    }
}
