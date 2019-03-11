package com.mermaid.framework.core.config.factory;

import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.util.Properties;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 10:17
 */
public class CommandLineConfigFactory extends AbstractConfigFactory {
    public CommandLineConfigFactory(String[] args) {
        SimpleCommandLinePropertySource simpleCommandLinePropertySource = new SimpleCommandLinePropertySource(args);
        String[] propertiesNames = simpleCommandLinePropertySource.getPropertyNames();
        Properties properties = new Properties();
        for(String propertiesName : propertiesNames) {
            properties.setProperty(propertiesName,simpleCommandLinePropertySource.getProperty(propertiesName));
        }
        addConfigs(properties);
    }
}
