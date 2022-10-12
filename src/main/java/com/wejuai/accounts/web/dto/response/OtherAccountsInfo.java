package com.wejuai.accounts.web.dto.response;

import com.wejuai.entity.mysql.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class OtherAccountsInfo {

    @ApiModelProperty("是否有该第三方帐号")
    private boolean has = true;
    @ApiModelProperty("第三方帐号类型")
    private OauthType type;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("昵称")
    private String nickName;

    public OtherAccountsInfo(OtherUser otherUser) {
        if (otherUser == null) {
            this.has = false;
            return;
        }
        this.type = otherUser.getOauthType();
        this.avatar = otherUser.getAvatar();
        this.nickName = otherUser.getNickName();
    }

    public OtherAccountsInfo(OauthType type) {
        this.has = false;
        this.type = type;
    }

    public OauthType getType() {
        return type;
    }

    public boolean getHas() {
        return has;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OtherAccountsInfo that = (OtherAccountsInfo) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
