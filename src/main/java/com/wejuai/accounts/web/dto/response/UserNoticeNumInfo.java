package com.wejuai.accounts.web.dto.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class UserNoticeNumInfo {

    @ApiModelProperty("一级评论数")
    private long comment;

    @ApiModelProperty("二级评论数")
    private long subComment;

    @ApiModelProperty("收到艾特数")
    private long remind;

    @ApiModelProperty("系统消息数")
    private long systemMessage;

    public UserNoticeNumInfo(long comment, long subComment, long remind, long systemMessage) {
        this.comment = comment;
        this.subComment = subComment;
        this.remind = remind;
        this.systemMessage = systemMessage;
    }

    UserNoticeNumInfo(){}

    public long getComment() {
        return comment;
    }

    public long getSubComment() {
        return subComment;
    }

    public long getRemind() {
        return remind;
    }

    public long getSystemMessage() {
        return systemMessage;
    }
}
