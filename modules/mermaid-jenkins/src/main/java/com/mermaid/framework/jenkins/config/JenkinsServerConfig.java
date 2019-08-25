package com.mermaid.framework.jenkins.config;

import com.offbytwo.jenkins.JenkinsServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
    @Value("${mermaid.jenkins.server.urls}")
    private String serverUrls;

    @Value("${mermaid.jenkins.server.username}")
    private String username;

    @Value("${mermaid.jenkins.server.passwordOrToken}")
    private String passwordOrToken;

    private static List<JenkinsServer> jenkinsServerList = new ArrayList<>();

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
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(serverUrls),username,passwordOrToken);
            jenkinsServerList.add(jenkinsServer);
        } else {
            String[] urls = serverUrls.split(",");
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
}
