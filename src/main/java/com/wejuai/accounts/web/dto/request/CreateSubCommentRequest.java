package com.wejuai.accounts.web.dto.request;

import com.wejuai.dto.request.SaveSubCommentRequest;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ZM.Wang
 */
public class CreateSubCommentRequest {

    @NotNull(message = "应用类型必填")
    @ApiModelProperty("应用类型")
    private AppType appType;

    @NotBlank(message = "应用id必填")
    @ApiModelProperty("应用id")
    private String appId;

    @NotBlank(message = "内容必填")
    @ApiModelProperty("内容")
    private String text;

    @NotBlank(message = "一级评论id必填")
    @ApiModelProperty("一级评论id")
    private String commentId;

    public SaveSubCommentRequest getCoreReq(User user) {
        return new SaveSubCommentRequest(user.getId(), appType, appId, text, commentId);
    }

    public AppType getAppType() {
        return appType;
    }

    public String getAppId() {
        return appId;
    }

    public String getText() {
        return text;
    }

    public String getCommentId() {
        return commentId;
    }
}
