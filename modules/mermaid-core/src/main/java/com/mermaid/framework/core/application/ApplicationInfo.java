package com.mermaid.framework.core.application;

import com.mermaid.framework.core.config.factory.ConfigFactory;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/20 23:07
 * version 1.0
 */
public class ApplicationInfo implements ApplicationDescriptor {

    /**服务超时时间，1000毫秒*/
    public static final long timeout = 1000;

    protected String appId;

    protected String appName;

    protected String appVersion;

    protected String appHost;

    protected int appPort;

    protected String appContextPath = "/";

    protected int pid;

    //系统启动时间, 近似于ApplicationInfo实例创建时间
    protected long launchTime;

    /**服务权重*/
    protected int weight;

//    private CloudClient cloudClient;

    private ConfigFactory runtimeProperties;

    protected static final ApplicationInfo instance = new ApplicationInfo();

    private State state = State.Starting;

    private ApplicationInfo(){}

    public static ApplicationInfo getInstance() {
        return instance;
    }
    @Override
    public String getAppName() {
        return this.appName;
    }

    @Override
    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppHost() {
        return appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public int getAppPort() {
        return appPort;
    }

    public void setAppPort(int appPort) {
        this.appPort = appPort;
    }

    public String getAppContextPath() {
        return appContextPath;
    }

    public void setAppContextPath(String appContextPath) {
        this.appContextPath = appContextPath;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

//    public CloudClient getCloudClient() {
//        return cloudClient;
//    }
//
//    public void setCloudClient(CloudClient cloudClient) {
//        this.cloudClient = cloudClient;
//    }

    public ConfigFactory getRuntimeProperties() {
        return runtimeProperties;
    }

    public void setRuntimeProperties(ConfigFactory runtimeProperties) {
        this.runtimeProperties = runtimeProperties;
    }

    //服务状态
    public enum State {
        Starting/**表示应用实例正在启动, 不接受请求*/
        ,
        Running/**表示应用实例处于运行状态, 不接受请求*/
        ,
        HangingUp/**表示应用实例处于挂起状态, 不接受请求*/
        ,
        Halt/**表示应用实例处于停机状态, 此状态为虚拟状态, 使用Zookeeper的临时ZNode管理*/
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
