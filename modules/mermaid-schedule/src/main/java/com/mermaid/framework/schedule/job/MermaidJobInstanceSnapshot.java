package com.mermaid.framework.schedule.job;

import java.util.Arrays;
import java.util.Date;

/**
 * Job 实例快照
 */
public class MermaidJobInstanceSnapshot {
    /** 主键 */
    private long id;

    /** 创建时间 */
    private Date gmtCreate;

    /** 修改时间 */
    private Date gmtModified;

    /** job的ID */
    private long jobId;

    /** 启动时间 */
    private String fireTime;

    /** 实例状态 */
    private int status;

    /** 实例描述 */
    private String description;

    /** 实例执行结果 */
    private String jobInstanceResult;

    /** 用户自定义全局变量 */
    private byte[] instanceGlobal;

    // 拉取位点
    private long offset;

    // 乐观锁版本号
    private long lockVersion;

    /** 重试次数 */
    private int retryCount;

    /** 下次重试时间 */
    private Date nextRetryTime;

    /** 通知版本 */
    private long notifyVersion;

    /** 通知依赖Job */
    private int relationTag;

    /** 结果更新版本 */
    private long resultVersion;

    /** 悲观锁状态 */
    private boolean isLocked = false;

    /** 锁定开始时间*/
    private Date gmtLocked;

    //信息
    private String information;

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

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getFireTime() {
        return fireTime;
    }

    public void setFireTime(String fireTime) {
        this.fireTime = fireTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJobInstanceResult() {
        return jobInstanceResult;
    }

    public void setJobInstanceResult(String jobInstanceResult) {
        this.jobInstanceResult = jobInstanceResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getInstanceGlobal() {
        return instanceGlobal;
    }

    public void setInstanceGlobal(byte[] instanceGlobal) {
        this.instanceGlobal = instanceGlobal;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(long lockVersion) {
        this.lockVersion = lockVersion;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public long getNotifyVersion() {
        return notifyVersion;
    }

    public void setNotifyVersion(long notifyVersion) {
        this.notifyVersion = notifyVersion;
    }

    public int getRelationTag() {
        return relationTag;
    }

    public void setRelationTag(int relationTag) {
        this.relationTag = relationTag;
    }

    public long getResultVersion() {
        return resultVersion;
    }

    public void setResultVersion(long resultVersion) {
        this.resultVersion = resultVersion;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Date getGmtLocked() {
        return gmtLocked;
    }

    public void setGmtLocked(Date gmtLocked) {
        this.gmtLocked = gmtLocked;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return "MermaidJobInstanceSnapshot{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", jobId=" + jobId +
                ", fireTime='" + fireTime + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", jobInstanceResult='" + jobInstanceResult + '\'' +
                ", instanceGlobal=" + Arrays.toString(instanceGlobal) +
                ", offset=" + offset +
                ", lockVersion=" + lockVersion +
                ", retryCount=" + retryCount +
                ", nextRetryTime=" + nextRetryTime +
                ", notifyVersion=" + notifyVersion +
                ", relationTag=" + relationTag +
                ", resultVersion=" + resultVersion +
                ", isLocked=" + isLocked +
                ", gmtLocked=" + gmtLocked +
                ", information='" + information + '\'' +
                '}';
    }
}
