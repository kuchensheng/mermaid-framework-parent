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
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.List;


@ComponentScan({"com.mermaid.framework"})
@SpringBootApplication
@EnableSwagger2
public class MermaidApplicationEntry {

    private static final String CONN_SERVER = "118.178.186.33:2181";
    private static final Integer SESSION_TIMEOUT = 50000;
    public static void main(String[] args) {

//        IRegisterCenter4Provider provider = new RegisterCenter();
//        provider.registerProvoder();
        SpringApplication.run(MermaidApplicationEntry.class,args);
    }
}
