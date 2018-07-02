package com.mermaid.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/1 23:59
 */
@ConfigurationProperties("zuul.filter")
public class FilterConfiguration {
    private String root;
    private Integer interval;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }
}
