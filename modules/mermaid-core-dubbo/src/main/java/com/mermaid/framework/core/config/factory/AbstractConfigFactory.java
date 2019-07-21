package com.mermaid.framework.core.config.factory;

import java.util.Properties;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 10:00
 */
public abstract class AbstractConfigFactory implements ConfigFactory{
    private final Properties config = new Properties();

    @Override
    public boolean constainsKey(String key) {
        return config.containsKey(key);
    }

    @Override
    public String getValue(String key) {
        return getValue(key,null);
    }

    @Override
    public String getValue(String key, String defaultValue) {
        return config.getProperty(key,defaultValue);
    }

    @Override
    public Properties getProperties() {
        return config;
    }

    protected void addConfigs(Properties configs) {
        this.config.putAll(configs);
    }

    @Override
    public String toString() {
        return getProperties().toString();
    }
}
