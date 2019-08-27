package com.mermaid.framework.jenkins.config;

import com.offbytwo.jenkins.JenkinsServer;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/26 20:00
 * version 1.0
 */
@Configuration
public class JenkinsServerConfig {

    /**
     * jenkins服务器地址，多个服务器地址用","隔开
     */
    @Value("${mermaid.jenkins.server.urls:http://ci.songxiaocai.net}")
    private String serverUrls;

    @Value("${mermaid.jenkins.server.username:admin}")
    private String username;

    @Value("${mermaid.jenkins.server.passwordOrToken:123456}")
    private String passwordOrToken;

    @Value("${mermaid.jenkins.callback.url:http://localhost:8080}")
    private String callbackurl;

    private static List<JenkinsServer> jenkinsServerList;

    @PostConstruct
    public void init() throws URISyntaxException {
        if(StringUtils.isEmpty(serverUrls)) {
            throw new RuntimeException("jenkins服务器地址不能为空");
        }
        if(StringUtils.isEmpty(username)) {
            throw new RuntimeException("jenkins用户名不能为空");
        }
        if(StringUtils.isEmpty(passwordOrToken)) {
            throw new RuntimeException("jenkins密码不能为空");
        }

        //验证是否有多个地址
        if(!serverUrls.contains(",")) {
            jenkinsServerList = new ArrayList<>(1);
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(serverUrls),username,passwordOrToken);
            jenkinsServerList.add(jenkinsServer);
        } else {
            String[] urls = serverUrls.split(",");
            jenkinsServerList = new ArrayList<>(urls.length);
            for (String url : urls) {
                JenkinsServer jenkinsServer = new JenkinsServer(new URI(url),username,passwordOrToken);
                jenkinsServerList.add(jenkinsServer);
            }
        }

    }

    public static List<JenkinsServer> getJenkinsServerList() {
        return jenkinsServerList;
    }

    public static void setJenkinsServerList(List<JenkinsServer> jenkinsServerList) {
        JenkinsServerConfig.jenkinsServerList = jenkinsServerList;
    }

    @Bean
    @ConditionalOnBean(JenkinsServerConfig.class)
    public JenkinsServer jenkinsServer() {
        CopyOnWriteArrayList<JenkinsServer> jenkinsServers = new CopyOnWriteArrayList<>(getJenkinsServerList());
        //TODO 随机选择机制
        Random random = new Random();
        for (int i=0;i<jenkinsServers.size();i++) {
            int n = random.nextInt(jenkinsServers.size());
            JenkinsServer jenkinsServer = jenkinsServers.get(n);
            if (jenkinsServer.isRunning()) {
                return jenkinsServer;
            } else {
                jenkinsServers.remove(n);
            }
        }

        throw new RuntimeException("Jenkins服务异常，请检查");
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }
}
