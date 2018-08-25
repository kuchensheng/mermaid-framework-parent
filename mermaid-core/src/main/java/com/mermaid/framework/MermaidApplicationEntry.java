package com.mermaid.framework;

import com.mermaid.framework.cloud.IRegisterCenter4Provider;
import com.mermaid.framework.cloud.RegisterCenter;
import com.mermaid.framework.cloud.constant.CloudZonePattern;
import com.mermaid.framework.cloud.module.ProviderService;
import com.mermaid.framework.util.IPUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.List;


@ComponentScan({"com"})
@EnableFeignClients
@SpringBootApplication
public class MermaidApplicationEntry {

    public static void main(String[] args) {

        SpringApplication.run(MermaidApplicationEntry.class,args);
    }
}
