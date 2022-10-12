package com.wejuai.accounts.web.dto.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class UserDetailedInfo {

    @ApiModelProperty("获得点赞数")
    private final long starNum;
    @ApiModelProperty("被收藏数量")
    private final long beCollectNum;
    @ApiModelProperty("收藏数量")
    private final long collectNum;
    @ApiModelProperty("关注人数量")
    private final long attentionNum;
    @ApiModelProperty("被关注数量")
    private final long followNum;
    @ApiModelProperty("积分")
    private final long integral;
    @ApiModelProperty("头衔数量")
    private final long titleNum;
    @ApiModelProperty("文章数量")
    private final long article;
    @ApiModelProperty("草稿数量")
    private final long articleDraft;
    @ApiModelProperty("悬赏数量")
    private final long rewardDemand;

    public UserDetailedInfo(long starNum, long collectNum, long beCollectNum, long attentionNum, long followNum, long integral, long titleNum, long article, long articleDraft, long rewardDemand) {
        this.starNum = starNum;
        this.beCollectNum = beCollectNum;
        this.collectNum = collectNum;
        this.attentionNum = attentionNum;
        this.followNum = followNum;
        this.integral = integral;
        this.titleNum = titleNum;
        this.article = article;
        this.articleDraft = articleDraft;
        this.rewardDemand = rewardDemand;
    }

    public long getStarNum() {
        return starNum;
    }

    public long getCollectNum() {
        return collectNum;
    }

    public long getBeCollectNum() {
        return beCollectNum;
    }

    public long getAttentionNum() {
        return attentionNum;
    }

    public long getFollowNum() {
        return followNum;
    }

    public long getIntegral() {
        return integral;
    }

    public long getTitleNum() {
        return titleNum;
    }

    public long getArticle() {
        return article;
    }

    public long getArticleDraft() {
        return articleDraft;
    }

    public long getRewardDemand() {
        return rewardDemand;
    }
}
