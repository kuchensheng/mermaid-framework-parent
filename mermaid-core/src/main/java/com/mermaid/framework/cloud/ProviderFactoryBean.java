package com.mermaid.framework.cloud;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/19 22:46
 * version 1.0
 */
@Slf4j
public class ProviderFactoryBean implements InitializingBean {

    @Value("${spring.application.index:9000}")
    private String serverPort;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("注册到Zookeeper，端口为{}",serverPort);

    }

}
