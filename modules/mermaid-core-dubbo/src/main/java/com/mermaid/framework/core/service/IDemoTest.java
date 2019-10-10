package com.mermaid.framework.core.service;

import com.mermaid.framework.core.mvc.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "测试接口")
public interface IDemoTest {

    @ApiOperation(value = "下单接口",notes = "下单接口")
    public APIResponse<OrderDTO> testOrder();


}
