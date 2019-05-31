package com.mermaid.framework.rabbitmq.support;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 杭州蓝诗网络科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project:hades
 * @CreateDate Created in 2019/5/31 10:17 <br>
 * @Author:<a href ="kuchensheng@quannengzhanggui.cn">kuchensheng</a>
 */
public class RunTimeUtil {

    private static AtomicInteger index = new AtomicInteger();

    public static int getPid() {
        String info = getRunTime();
        int pid = new Random().nextInt();
        int index = info.indexOf("@");
        if(index > 0) {
            pid = Integer.parseInt(info.substring(0,index));
        }
        return pid;
    }

    private static String getRunTime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getName();
    }

    public static String getRocketMqUniqeInstanceName() {
        return "pid" + getPid() + "_index" + index.incrementAndGet();
    }
}
