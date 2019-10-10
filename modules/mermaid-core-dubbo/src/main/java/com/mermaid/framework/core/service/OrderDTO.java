package com.mermaid.framework.core.service;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName:OrderDTO
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  10:18
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 10:18   kuchensheng    1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @ApiModelProperty(value = "订单号",name = "orderId",example = "o123456")
    private String orderId;

    @ApiModelProperty(value = "订单名称",name = "orderName",example = "oKr")
    private String orderName;

}
