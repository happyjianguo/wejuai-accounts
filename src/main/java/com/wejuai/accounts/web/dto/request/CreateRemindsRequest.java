package com.wejuai.accounts.web.dto.request;

import com.wejuai.dto.request.SaveRemindsRequest;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ZM.Wang
 */
public class CreateRemindsRequest {

    @Size(min = 1, message = "被艾特的人最少一个")
    @ApiModelProperty("被艾特的人的id")
    private List<String> receiver;

    @NotNull(message = "应用类型必填")
    @ApiModelProperty("应用类型")
    private AppType appType;

    @NotBlank(message = "应用id必填")
    @ApiModelProperty("应用id")
    private String appId;

    @NotBlank(message = "内容必填")
    @ApiModelProperty("内容")
    private String text;

    @ApiModelProperty("一级评论id，如果有的话填写")
    private String commentId;

    public SaveRemindsRequest getCoreReq(User user) {
        return new SaveRemindsRequest(user.getId(), receiver, appType, appId, text, commentId);
    }

    public List<String> getReceiver() {
        return receiver;
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
