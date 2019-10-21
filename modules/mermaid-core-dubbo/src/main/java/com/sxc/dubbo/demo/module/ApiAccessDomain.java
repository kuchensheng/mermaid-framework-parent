package com.sxc.dubbo.demo.module;

import java.io.Serializable;

public class ApiAccessDomain implements Serializable {
    private Integer id;

    private String apiName;

    private String clientIp;

    private String clientVersion;

    private String clientSysName;

    private String clientSysVersion;

    private Long userId;

    private String userNick;

    private Integer costTime;

    private Integer errorCode;

    private String subCode;

    private Long callTime;

    private Long appKey;

    private String deviceUuid;

    private Integer platformCostTime;

    private String traceId;

    private Byte archives;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName == null ? null : apiName.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion == null ? null : clientVersion.trim();
    }

    public String getClientSysName() {
        return clientSysName;
    }

    public void setClientSysName(String clientSysName) {
        this.clientSysName = clientSysName == null ? null : clientSysName.trim();
    }

    public String getClientSysVersion() {
        return clientSysVersion;
    }

    public void setClientSysVersion(String clientSysVersion) {
        this.clientSysVersion = clientSysVersion == null ? null : clientSysVersion.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick == null ? null : userNick.trim();
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode == null ? null : subCode.trim();
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }

    public Long getAppKey() {
        return appKey;
    }

    public void setAppKey(Long appKey) {
        this.appKey = appKey;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid == null ? null : deviceUuid.trim();
    }

    public Integer getPlatformCostTime() {
        return platformCostTime;
    }

    public void setPlatformCostTime(Integer platformCostTime) {
        this.platformCostTime = platformCostTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId == null ? null : traceId.trim();
    }

    public Byte getArchives() {
        return archives;
    }

    public void setArchives(Byte archives) {
        this.archives = archives;
    }
}