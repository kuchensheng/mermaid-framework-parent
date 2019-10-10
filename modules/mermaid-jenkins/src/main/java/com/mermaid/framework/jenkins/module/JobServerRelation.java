package com.mermaid.framework.jenkins.module;

import com.offbytwo.jenkins.JenkinsServer;

/**
 * ClassName:JobServerRelation
 * Description: 任务与服务的关系
 *
 * @author: kuchensheng
 * @version: Create at:  11:52
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 11:52   kuchensheng    1.0
 */
public class JobServerRelation {

    /**
     * 构建的序号
     */
    private Integer buildNumber;

    /**
     * job名称
     */
    private String jobName;

    /**
     * 这个job的此次构建所在的Jenkins服务
     */
    private JenkinsServer jenkinsServer;

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public JenkinsServer getJenkinsServer() {
        return jenkinsServer;
    }

    public void setJenkinsServer(JenkinsServer jenkinsServer) {
        this.jenkinsServer = jenkinsServer;
    }
}
