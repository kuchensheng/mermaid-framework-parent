package com.mermaid.framework.azkaban.modules;

/**
 * ClassName:JobDomain
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  10:37
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 10:37   kuchensheng    1.0
 */

public class JobDomain {

    private String jobName;

    private JobTypeEnum jobType;

    private String[] commands;

    private String[] dependencies;

    private String callbackJobId;


    enum JobTypeEnum {
        COMMAND("command"),JAVA("java");
        private String value;

        JobTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    public String getCallbackJobId() {
        return callbackJobId;
    }

    public void setCallbackJobId(String callbackJobId) {
        this.callbackJobId = callbackJobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
