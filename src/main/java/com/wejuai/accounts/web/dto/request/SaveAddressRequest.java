package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class SaveAddressRequest {

    @ApiModelProperty("id，选填修改时填")
    private String id;

    @NotBlank(message = "手机号必填")
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("省名称")
    @NotBlank(message = "省必选")
    private String province;

    @ApiModelProperty("城市名称")
    @NotBlank(message = "城市必选")
    private String city;

    @ApiModelProperty("县市区名")
    @NotBlank(message = "县(市/区)必选")
    private String region;

    @ApiModelProperty("详细地址")
    @NotBlank(message = "详细地址必填")
    private String detailed;

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getDetailed() {
        return detailed;
    }
}
