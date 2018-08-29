package com.mermaid.framework;

import com.mermaid.framework.mvc.APIResponse;
import com.mermaid.framework.redis.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/30 0:14
 * version 1.0
 */
@RestController
@Api("测试入口")
public class TestController {
    @Autowired
    private RedisService redisService;

    @ApiOperation("测试redis")
    @RequestMapping(value = "/app/test/redis/set",method = RequestMethod.PUT)
    public APIResponse testRedisSet(
            @ApiParam(required = true,name = "key",value = "键") @RequestParam(value = "key") String key,
            @ApiParam(required = true,name = "value",value = "值") @RequestParam(value = "value") String value
    ) {
        redisService.set(key,value);
        return APIResponse.success();
    }
}
