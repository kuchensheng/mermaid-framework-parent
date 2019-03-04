package com.mermaid.framework.dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 14:57
 */
@SpringBootApplication
@ImportResource(value = "classpath:META-INF/dubbo-provider.xml")
//@PropertySource("META-INF/mermaid-framework-dubbo.properties")
public class DubboApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboApplication.class,args);
    }
}
