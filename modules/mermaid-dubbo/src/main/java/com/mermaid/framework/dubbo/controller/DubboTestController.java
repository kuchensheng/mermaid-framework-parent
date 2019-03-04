package com.mermaid.framework.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mermaid.framework.dubbo.consumer.DubboConsumerService;
import com.mermaid.framework.dubbo.service.DubboTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/4 14:45
 */
@RestController
public class DubboTestController {

    @Autowired
    private DubboConsumerService consumerService;

    @RequestMapping(value = "/dubbo/test",method = RequestMethod.GET)
    public void test() {
        consumerService.printMessage();
    }

}
