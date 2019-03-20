package com.mermaid.framework.redis;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/20 15:27
 */
public class RedisDistributeLockResult {
    private String lockName;
    private boolean isSuccess;
    private String idenfier;
    private RedisDistributeLockObject dlock;

    public RedisDistributeLockResult(String lockName, boolean isSuccess, String idenfier, RedisDistributeLockObject dlock) {
        this.lockName = lockName;
        this.isSuccess = isSuccess;
        this.idenfier = idenfier;
        this.dlock = dlock;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getIdenfier() {
        return idenfier;
    }

    public void setIdenfier(String idenfier) {
        this.idenfier = idenfier;
    }

    public RedisDistributeLockObject getDlock() {
        return dlock;
    }

    public void setDlock(RedisDistributeLockObject dlock) {
        this.dlock = dlock;
    }

    @Override
    public String toString() {
        return "RedisDistributeLockResult{" +
                "lockName='" + lockName + '\'' +
                ", isSuccess=" + isSuccess +
                ", idenfier='" + idenfier + '\'' +
                ", dlock=" + dlock +
                '}';
    }
}
