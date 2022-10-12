package com.wejuai.accounts.web.dto.request;

import com.wejuai.entity.mongo.AppType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ZM.Wang
 */
public class CreateCommentRequest {

    @ApiModelProperty("应用类型")
    @NotNull(message = "应用类型不允许为空")
    private AppType appType;
    @ApiModelProperty("应用id")
    @NotBlank(message = "应用id不允许为空")
    private String appId;
    @ApiModelProperty("评论内容")
    @NotBlank(message = "评论内容不允许为空")
    private String text;

    @ApiModelProperty("被@用户的id")
    private Set<String> notices;

    public AppType getAppType() {
        return appType;
    }

    public String getAppId() {
        return appId;
    }

    public String getText() {
        return text;
    }

    public Set<String> getNotices() {
        return notices;
    }
}
