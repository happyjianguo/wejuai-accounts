package com.wejuai.accounts.web.dto.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class WxMiniAppLoginInfo {

    @ApiModelProperty("是否有unionId，返回false需要拉起公众号登录")
    private final boolean hasUnionId;

    public WxMiniAppLoginInfo(boolean hasUnionId) {
        this.hasUnionId = hasUnionId;
    }

    public boolean getHasUnionId() {
        return hasUnionId;
    }
}
