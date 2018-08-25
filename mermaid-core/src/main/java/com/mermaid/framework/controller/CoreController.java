package com.mermaid.framework.controller;

import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
//@RequestMapping(value = "/core")
@Api(value = "框架核心配置",tags = "框架核心配置")
public class CoreController {

    private static final Logger logger = LoggerFactory.getLogger(CoreController.class);

    @Autowired
    private Environment environment;

    @ApiOperation(value = "获取框架配置",notes = "获取mermaid框架配置")
    @RequestMapping(value = "/core/get",method = RequestMethod.GET)
    public void getCoreConfig(){


    }
    @ApiOperation(value = "获取负载列表",notes = "获取Ribbon ’s mermaid框架配置")
    @RequestMapping(value = "/core/getBalence",method = RequestMethod.GET)
    public String getCoreConfigByBalence(){

//        logger.info("访问了端口：{}",environment.getProperty("server.port"));
//        return "I am ok "+environment.getProperty("server.port");
        return "I am O";
    }

    @ApiOperation(value = "测试swagger")
    @RequestMapping(value = "/core/swagger2",method = RequestMethod.GET)
    public String swagger2Test() {
        return "swagger2.html";
    }

}
