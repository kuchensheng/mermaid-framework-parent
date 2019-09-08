package com.mermaid.framework.jenkins.core;

import com.mermaid.framework.jenkins.config.JenkinsServerConfig;
import com.mermaid.framework.jenkins.module.EnumJobType;
import com.mermaid.framework.jenkins.util.JenkinsPipelineUtil;
import com.mermaid.framework.util.IPAddress;
import com.mermaid.framework.util.IPAddressUtils;
import com.offbytwo.jenkins.JenkinsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/25 22:39
 * version 1.0
 */
@Component
public class Operations {

    /**
     * 各个模块回调函数的参数
     */
    private static final String PARAMETERS = "?jobName=${env.JOB_NAME}&buildNumber=${env.BUILD_NUMBER}&buildResult=${currentBuild.currentResult}";

    @Autowired
    private JenkinsServer jenkinsServer;

    @Autowired
    private JenkinsServerConfig jenkinsServerConfig;

    @Value("${server.port:8080}")
    private String serverPort;


    /**
     * 创建maven 打包 发布任务
     * @param jobName jenkins任务名称
     * @param description 描述
     * @param gitUrl git地址
     * @param branch 打包的分支
     */
    public void createMavenDeployJob(String jobName, String description, String gitUrl, String branch, EnumJobType jobType) {
        //根据参数信息组件pipeline
        Map<String,Object> rootMap = new HashMap<>();
        rootMap.put("gitUrl",gitUrl);
        rootMap.put("branch",branch);
        rootMap.put("description",description);
        if(null == jobType) {
            jobType = EnumJobType.SNAPSHOTS;
        }
        rootMap.put("deployType",jobType.getValue());
        if("1".equals(jobType.getValue())) {
            rootMap.put("mvnDeployCmd","mvn -B -U verify");
            rootMap.put("mvnCleanCmd","mvn org.apache.maven.plugins:maven-release-plugin:2.5.2:clean");
            rootMap.put("mvnPrepareCmd","mvn org.apache.maven.plugins:maven-release-plugin:2.5.2:prepare");
            rootMap.put("mvnStageCmd","org.apache.maven.plugins:maven-release-plugin:2.5.2:stage");
            rootMap.put("release_rollback","mvn release:rollback");
        }else {
            rootMap.put("mvnDeployCmd","mvn -B -U verify clean deploy ");
        }

        try {
            rootMap.put("callbackUrl",getCallbackurl());
            rootMap.put("parameter",PARAMETERS);
            String configXml = JenkinsPipelineUtil.createMavenJobConfigXml(rootMap);
            jenkinsServer.createJob(jobName,configXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCallbackurl() throws Exception {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://").append(IPAddressUtils.getLocalIP()).append(":").append(serverPort);
        return urlBuilder.toString();
    }


}
