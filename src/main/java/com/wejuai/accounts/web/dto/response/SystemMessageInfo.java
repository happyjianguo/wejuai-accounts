package com.wejuai.accounts.web.dto.response;

import com.wejuai.entity.mongo.SystemMessage;
import io.swagger.annotations.ApiModelProperty;

public class SystemMessageInfo {

    private final String id;
    private final long createdAt;
    @ApiModelProperty("内容")
    private final String text;
    @ApiModelProperty("是否已读")
    private final boolean watch;
    private final String userId;
    @ApiModelProperty("是否群发")
    private final boolean groupPush;

    public SystemMessageInfo(SystemMessage systemMessage) {
        this.id = systemMessage.getId();
        this.createdAt = systemMessage.getCreatedAt().getTime();
        this.text = systemMessage.getText();
        this.watch = systemMessage.getWatch();
        this.userId = systemMessage.getUserId();
        this.groupPush = systemMessage.getGroupPush();
    }

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public boolean getWatch() {
        return watch;
    }

    public String getUserId() {
        return userId;
    }

    public boolean getGroupPush() {
        return groupPush;
    }
}
