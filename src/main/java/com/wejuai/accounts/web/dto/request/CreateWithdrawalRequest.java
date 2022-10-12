package com.wejuai.accounts.web.dto.request;

import com.wejuai.entity.mysql.WithdrawalType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author ZM.Wang
 */
public class CreateWithdrawalRequest {

    @Min(value = 1000, message = "最小提现积分1000")
    @ApiModelProperty("积分数")
    private long integral;

    @NotNull(message = "通道类型必选")
    @ApiModelProperty("通道类型")
    private WithdrawalType channelType;

    @ApiModelProperty("支付宝必填名字")
    private String name;

    @ApiModelProperty("支付宝必填账号")
    private String cardNo;

    public long getIntegral() {
        return integral;
    }

    public CreateWithdrawalRequest setIntegral(long integral) {
        this.integral = integral;
        return this;
    }

    public WithdrawalType getChannelType() {
        return channelType;
    }

    public CreateWithdrawalRequest setChannelType(WithdrawalType channelType) {
        this.channelType = channelType;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateWithdrawalRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getCardNo() {
        return cardNo;
    }

    public CreateWithdrawalRequest setCardNo(String cardNo) {
        this.cardNo = cardNo;
        return this;
    }
}
