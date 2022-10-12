package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author ZM.Wang
 */
public class CreateEvaluateRequest {

    @ApiModelProperty("文章id")
    private String appId;

    @ApiModelProperty("悬赏的提交id")
    private String rewardSubmissionId;

    @ApiModelProperty("分数")
    @Min(value = 1, message = "评分最小1分")
    @Max(value = 10, message = "评分最大10分")
    private long score;

    @ApiModelProperty("评价内容")
    private String content;

    public String getAppId() {
        return appId;
    }

    public CreateEvaluateRequest setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public long getScore() {
        return score;
    }

    public CreateEvaluateRequest setScore(long score) {
        this.score = score;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CreateEvaluateRequest setContent(String content) {
        this.content = content;
        return this;
    }

    public String getRewardSubmissionId() {
        return rewardSubmissionId;
    }

    public CreateEvaluateRequest setRewardSubmissionId(String rewardSubmissionId) {
        this.rewardSubmissionId = rewardSubmissionId;
        return this;
    }
}
