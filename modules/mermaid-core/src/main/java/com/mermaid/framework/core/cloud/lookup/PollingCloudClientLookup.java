package com.mermaid.framework.core.cloud.lookup;

import com.mermaid.framework.core.application.ApplicationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Desription:依次获取服务提供者列表中的数据，并使用计数器记录使用过的数据索引
 *
 * @author:Hui CreateDate:2019/2/23 11:16
 * version 1.0
 */
public class PollingCloudClientLookup implements CloudClientLookupStrategy {
    private static final Logger logger = LoggerFactory.getLogger(PollingCloudClientLookup.class);

    private int index = 0;
    private Lock lock = new ReentrantLock();
    @Override
    public ApplicationInfo select(List<ApplicationInfo> providerServices) {
        ApplicationInfo applicationInfo = null;
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            //若计数大于服务提供者个数，则计数归零
            if(index >= providerServices.size()) {
                index = 0;
            }
            applicationInfo = providerServices.get(index);
            index ++;
        } catch (InterruptedException e) {
            logger.error("轮询获取服务实例错误",e);
        } finally {
            lock.unlock();
        }

        if(null == applicationInfo) {
            return providerServices.get(0);
        }
        return applicationInfo;
    }
}
