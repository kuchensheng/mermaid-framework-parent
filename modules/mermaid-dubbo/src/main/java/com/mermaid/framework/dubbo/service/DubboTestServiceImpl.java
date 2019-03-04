package com.mermaid.framework.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 14:50
 */
@Service(interfaceClass = DubboTestService.class)
public class DubboTestServiceImpl implements DubboTestService {

    @Override
    public void soutInfo(String message) {
        System.out.println("这里是dubbo测试");
    }
}
