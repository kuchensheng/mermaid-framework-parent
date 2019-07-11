package com.mermaid.framework.schedule.job;

import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.schedule.Constants;

import java.util.Date;

public class MermaidJob implements Constants {
    /**
     * 主键 job的ID
     */
    private long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 服务端集群分组ID
     */
    private long serverGroupId;

    /**
     * 客户端集群ID
     */
    private long clientGroupId;

    /**
     * Job描述
     */
    private String description;

    /**
     * 创建者ID
     */
    private String createrId;

    /**
     * Job类型
     */
    private int type;

    /**
     * 时间表达式
     */
    private String cronExpression;

    /**
     * job处理器
     */
    private String jobProcessor;

    /**
     * 最大运行实例数量
     */
    private int maxInstanceAmount;

    /**
     * Job用户自定义参数
     */
    private String jobArguments;

    /**
     * Job状态
     */
    private int status = JOB_STATUS_ENABLE;

    //Job等级
    private int level;

    //最大线程数量
    private int maxThreads;

    /**
     * taskName
     */
    private String taskName;

    /**
     * edas group id
     */
    private String edasGroupId;
    /**
     * 集群代码
     */
    private String clusterCode;

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (!(obj instanceof MermaidJob)) {
            return false;
        }

        MermaidJob job = (MermaidJob) obj;
        if(!job.toString().equals(this.toString())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public static MermaidJob newInstance(String json) {
        return JSONObject.parseObject(json,MermaidJob.class);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public long getClientGroupId() {
        return clientGroupId;
    }

    public void setClientGroupId(long clientGroupId) {
        this.clientGroupId = clientGroupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobProcessor() {
        return jobProcessor;
    }

    public void setJobProcessor(String jobProcessor) {
        this.jobProcessor = jobProcessor;
    }

    public int getMaxInstanceAmount() {
        return maxInstanceAmount;
    }

    public void setMaxInstanceAmount(int maxInstanceAmount) {
        this.maxInstanceAmount = maxInstanceAmount;
    }

    public String getJobArguments() {
        return jobArguments;
    }

    public void setJobArguments(String jobArguments) {
        this.jobArguments = jobArguments;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getEdasGroupId() {
        return edasGroupId;
    }

    public void setEdasGroupId(String edasGroupId) {
        this.edasGroupId = edasGroupId;
    }

    public String getClusterCode() {
        return clusterCode;
    }

    public void setClusterCode(String clusterCode) {
        this.clusterCode = clusterCode;
    }
}
