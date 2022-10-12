package com.wejuai.accounts.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class PushWebLogRequest {

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("分支, 目前只有dev和prod")
    private String branch;

    @ApiModelProperty(hidden = true)
    @JsonProperty("commit_message")
    private String commentMsg;

    public String getContent() {
        return content;
    }

    public String getBranch() {
        return branch;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public PushWebLogRequest setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
        return this;
    }
}
