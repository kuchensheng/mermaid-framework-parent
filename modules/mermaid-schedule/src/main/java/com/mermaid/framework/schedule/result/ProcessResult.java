package com.mermaid.framework.schedule.result;

public class ProcessResult {
    private boolean success = true;
    private int retryCount;
    private long sleeptime = 100L;

    public ProcessResult() {
    }

    public ProcessResult(boolean success) {
        this.success = success;
    }

    public ProcessResult(boolean success, int retryCount) {
        this.success = success;
        this.retryCount = retryCount;
    }

    public ProcessResult(boolean success, long sleeptime) {
        this.success = success;
        this.sleeptime = sleeptime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(long sleeptime) {
        this.sleeptime = sleeptime;
    }

    @Override
    public String toString() {
        return "ProcessResult{" +
                "success=" + success +
                ", retryCount=" + retryCount +
                ", sleeptime=" + sleeptime +
                '}';
    }
}
