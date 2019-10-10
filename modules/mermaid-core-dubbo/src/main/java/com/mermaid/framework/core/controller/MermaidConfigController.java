package com.mermaid.framework.core.controller;

import com.mermaid.framework.registry.zookeeper.ZKClientWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ClassName:MermaidConfigController
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  16:49
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 16:49   kuchensheng    1.0
 */
@RestController
@ConditionalOnExpression("${mermaid.framework.cloud.enable:false} == true")
@Api
public class MermaidConfigController {

    @Autowired
    private ZKClientWrapper zkClientWrapper;

    @RequestMapping(value = "/core/config",method = RequestMethod.PUT)
    @ApiOperation(value = "测试")
    public void receiveChange(@ApiParam(value = "参数实体",name = "changedMap") @RequestBody Map<String,Object> changedMap) {

    }

    @RequestMapping(value = "/core/znode",method = RequestMethod.GET)
    public void updateZnode(@RequestParam String path,@RequestParam String data) {
        if(!zkClientWrapper.exists(path)) {
            zkClientWrapper.createPersistent(path,data);
        } else {
            zkClientWrapper.setData(path,data);
        }
    }
}
