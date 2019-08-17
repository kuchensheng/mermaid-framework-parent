package com.mermaid.framework.core.config.factory;

import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ClassName:MermaidCloudConfigFactory
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  16:07
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 16:07   kuchensheng    1.0
 */
public class MermaidCloudConfigFactory extends AbstractConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(MermaidCloudConfigFactory.class);

    private  Properties properties = GlobalRuntimeConfigFactory.getInstance().getProperties();

    private String lastUpdateVersion;

    @Autowired
    private RestTemplate restTemplate;

    public MermaidCloudConfigFactory() {
        addConfigs(getCloudConfig());
    }

    private Properties getCloudConfig() {
        String url = properties.getProperty("mermaid.cloud.url");
        long startTime = System.currentTimeMillis();
        String applicationName = properties.getProperty("spring.application.name");
        String ticket = properties.getProperty("mermaid.cloud.ticket");
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(applicationName) || StringUtils.isEmpty(ticket)) {
            logger.warn("正在以应用[{}]的身份[{}]连接到云平台[{}]",applicationName,ticket,url);
            throw new RuntimeException("配置异常，请检查");
        }
        logger.info("正在以应用[{}]的身份[{}]连接到云平台[{}]",applicationName,ticket,url);
        Map<String,Object> param = new HashMap<>();
        param.put("application.name",applicationName);
        param.put("ticket",ticket);
        Properties result = new Properties();
        JSONObject forObject = restTemplate.getForObject(url, JSONObject.class, param);
        if(null != forObject && !CollectionUtils.isEmpty(forObject)) {
            for (String key : forObject.keySet()) {
                result.put(key,forObject.get(key));
            }
        }
        logger.info("配置接受完毕，共耗时[{}]ms",(System.currentTimeMillis() - startTime));
        return result;
    }
}
