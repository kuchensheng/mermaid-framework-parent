package com.mermaid.framework.jenkins.core;

import com.mermaid.framework.AppTest;
import com.offbytwo.jenkins.JenkinsServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/27 23:05
 * version 1.0
 */
@Component
public class OperationsTest extends AppTest{

    @Resource
    private Operations operations;
    @Resource
    private JenkinsServer jenkinsServer;

    @Test
    public void createMavenDeployJob() throws Exception {
        operations.createMavenDeployJob("test","这里是测试","http://gitlab.songxiaocai.org/xiamengbing/bifrost.git","develop",null);
    }

    @Test
    public void createMavenDeployJob1() throws Exception {
        jenkinsServer.createJob("test",getXmlContent());
    }

    private String getXmlContent() {
        return null;
    }
}