package com.mermaid.framework.core.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class MermaidConfigController {

    @RequestMapping(value = "/core/config",method = RequestMethod.PUT)
    public void receiveChange(@RequestBody Map<String,Object> changedMap) {

    }
}
