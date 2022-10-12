package com.wejuai.accounts.web.dto.response;

import com.wejuai.dto.response.Slice;
import com.wejuai.dto.response.SubCommentInfo;
import com.wejuai.dto.response.UserBaseInfo;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class CommentInfo {

    @ApiModelProperty("评论id")
    private String id;
    @ApiModelProperty("创建时间")
    private String createdAt;
    @ApiModelProperty("评论内容")
    private String text;
    @ApiModelProperty("应用创建者信息")
    private UserBaseInfo appCreatorInfo;
    @ApiModelProperty("评论人信息")
    private UserBaseInfo senderInfo;
    @ApiModelProperty("点赞数")
    private long starNum;
    @ApiModelProperty("子回复分页信息")
    private Slice<SubCommentInfo> subComments;

    public String getId() {
        return id;
    }

    public long getStarNum() {
        return starNum;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public UserBaseInfo getAppCreatorInfo() {
        return appCreatorInfo;
    }

    public UserBaseInfo getSenderInfo() {
        return senderInfo;
    }

    public Slice<SubCommentInfo> getSubComments() {
        return subComments;
    }
}
