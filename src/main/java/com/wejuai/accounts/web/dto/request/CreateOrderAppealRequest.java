package com.wejuai.accounts.web.dto.request;

import com.wejuai.entity.mysql.OrdersPageType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author ZM.Wang
 */
public class CreateOrderAppealRequest {

    @ApiModelProperty("订单类型")
    private OrdersPageType type;

    @ApiModelProperty("订单id")
    private String typeId;

    @Length(min = 10, max = 200, message = "问题描述必须是10到200个字之间")
    @ApiModelProperty("遇到的问题")
    private String question;

    public OrdersPageType getType() {
        return type;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getQuestion() {
        return question;
    }
}
