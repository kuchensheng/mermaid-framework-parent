package com.mermaid.framework;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("core-service")
public interface MermaidInterface {
    @ApiOperation(value = "获取负载列表",notes = "获取Ribbon ’s mermaid框架配置")
    @RequestMapping(value = "sdk/getBalence",method = RequestMethod.GET)
    public String getCoreConfigByBalence();
}
