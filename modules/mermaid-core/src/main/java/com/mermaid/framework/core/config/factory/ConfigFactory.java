package com.mermaid.framework.core.config.factory;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/11 9:58
 */
public interface ConfigFactory extends Serializable {

    boolean constainsKey(String key);

    String getValue(String key);

    String getValue(String key,String defaultValue);

    Properties getProperties();
}
