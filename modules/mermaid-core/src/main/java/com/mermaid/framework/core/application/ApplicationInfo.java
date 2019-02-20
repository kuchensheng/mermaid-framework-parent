package com.mermaid.framework.core.application;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/2/20 23:07
 * version 1.0
 */
public class ApplicationInfo implements ApplicationDescriptor {

    protected String appId;

    protected String appName;

    protected String appVersion;

    protected String appHost;

    protected int appPort;

    protected String appContextPath = "/";

    protected String sweetFrameworkVersion;

    protected int pid;

    //系统启动时间, 近似于ApplicationInfo实例创建时间
    protected long launchTime;

    protected static volatile ApplicationInfo instance = null;

    public static ApplicationInfo getInstance() {
        //全局唯一
        if(null == instance) {
            synchronized (ApplicationInfo.class) {
                if(null == instance) {
                    instance = new ApplicationInfo();
                }
            }
        }
        return instance;
    }


    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public String getAppId() {
        return null;
    }
}
