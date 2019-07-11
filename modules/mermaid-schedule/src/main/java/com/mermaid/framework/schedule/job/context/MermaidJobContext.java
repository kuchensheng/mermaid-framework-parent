package com.mermaid.framework.schedule.job.context;

import com.mermaid.framework.schedule.Constants;
import com.mermaid.framework.schedule.job.MermaidJob;
import com.mermaid.framework.schedule.job.MermaidJobInstanceSnapshot;

public class MermaidJobContext implements Constants {
    public MermaidJobContext() {
    }

    /** 当前Task的Job配置 */
    public MermaidJob job;

    /** 当前Task的Job实例 */
    public MermaidJobInstanceSnapshot jobInstanceSnapshot;

    /** 重试次数 */
    protected int retryCount;

    public MermaidJobContext(MermaidJob job, MermaidJobInstanceSnapshot jobInstanceSnapshot, int retryCount) {
        this.job = job;
        this.jobInstanceSnapshot = jobInstanceSnapshot;
        this.retryCount = retryCount;
    }

    public MermaidJob getJob() {
        return job;
    }

    public void setJob(MermaidJob job) {
        this.job = job;
    }

    public MermaidJobInstanceSnapshot getJobInstanceSnapshot() {
        return jobInstanceSnapshot;
    }

    public void setJobInstanceSnapshot(MermaidJobInstanceSnapshot jobInstanceSnapshot) {
        this.jobInstanceSnapshot = jobInstanceSnapshot;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
