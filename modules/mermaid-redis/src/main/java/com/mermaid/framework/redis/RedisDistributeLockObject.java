package com.mermaid.framework.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/20 10:35
 */
public class RedisDistributeLockObject {

    private long selfReleaseExpired;

    private String lockName;

    private static final String DLOCK_PREFIX="mermaid:dlock";

    private static final long DEFAULT_EXPIRED = 0L;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private final String identifier = UUID.randomUUID().toString();

    public RedisDistributeLockObject(String lockName) {
        this(lockName,DEFAULT_EXPIRED);
    }

    public RedisDistributeLockObject(String lockName,long expire) {
        this.selfReleaseExpired = expire;
        this.lockName = lockName;
    }

    public long getSelfReleaseExpired() {
        return selfReleaseExpired;
    }

    public void setSelfReleaseExpired(long selfReleaseExpired) {
        this.selfReleaseExpired = selfReleaseExpired;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
