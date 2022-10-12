package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class UpdateWxUserInfoRequest {

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("性别")
    private int sex;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickName;

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickName() {
        return nickName;
    }
}
