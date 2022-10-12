package com.wejuai.accounts.web.dto.request;

import com.wejuai.entity.mysql.Sex;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Created by ZM.Wang
 */
public class UpdateUserInfoRequest {

    @Length(max = 12, message = "昵称最多只能12个字哦")
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("生日，时间戳long型")
    private long birthday;
    @ApiModelProperty("性别")
    private Sex sex;
    @Length(max = 200, message = "个人简介只能200字哦")
    @ApiModelProperty("简介")
    private String inShort;
    @Length(max = 20, message = "可以随便填的所在地也只能20个字哦")
    @ApiModelProperty("可以随便填的所在地")
    private String location;

    public String getNickName() {
        return nickName;
    }

    public long getBirthday() {
        return birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public String getInShort() {
        return inShort;
    }

    public String getLocation() {
        return location;
    }
}
