package com.wejuai.accounts.web.dto.request;


import com.wejuai.dto.request.SaveRewardDemandRequest;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class CreateRewardDemandRequest {

    @NotBlank(message = "爱好类型必填")
    @ApiModelProperty("所属爱好的id")
    private String hobbyId;

    @NotBlank(message = "标题必填")
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("积分")
    @Min(value = 100, message = "创建悬赏悬赏最少100积分起")
    private long integral;

    @ApiModelProperty("缩略文本")
    private String inShort;

    @ApiModelProperty("内容")
    private String text;

    public SaveRewardDemandRequest getCoreReq(User user) {
        return new SaveRewardDemandRequest(user.getId(), hobbyId, title, inShort, text, integral);
    }

    public String getHobbyId() {
        return hobbyId;
    }

    public String getTitle() {
        return title;
    }

    public long getIntegral() {
        return integral;
    }

    public String getInShort() {
        return inShort;
    }

    public String getText() {
        return text;
    }
}
