package com.wejuai.accounts.web.dto.request;

import com.wejuai.dto.request.SaveRewardSubmissionRequest;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class CreateRewardSubmissionRequest {

    @ApiModelProperty("选填id，修改时添加")
    private String id;

    @ApiModelProperty("提交的悬赏id")
    @NotBlank(message = "悬赏id必填")
    private String rewardDemandId;

    @ApiModelProperty("提交内容的缩略版，选定前看到的部分")
    private String inShort;

    @ApiModelProperty("提交内容，选定后才能看到，富文本")
    @NotBlank(message = "提交内容必填")
    private String text;

    public SaveRewardSubmissionRequest getCoreReq(User user) {
        return new SaveRewardSubmissionRequest(id, user.getId(), rewardDemandId, inShort, text);
    }

    public String getId() {
        return id;
    }

    public String getRewardDemandId() {
        return rewardDemandId;
    }

    public String getText() {
        return text;
    }

    public String getInShort() {
        return inShort;
    }
}
