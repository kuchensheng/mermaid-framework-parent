package com.mermaid.framework.dubbo.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mermaid.framework.dubbo.service.DubboTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 14:54
 */
@Component
public class DubboConsumerService {

    @Reference(check = false)
    DubboTestService dubboTestService;

    public void printMessage() {
        dubboTestService.soutInfo("77777");
    }
}
