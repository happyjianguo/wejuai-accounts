package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class CreateArticleDraftRequest {

    private String id;
    @ApiModelProperty("所属爱好的id")
    private String hobbyId;
    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("封面图片的id")
    private String coverId;
    @ApiModelProperty("100字缩略内容")
    private String inShort;
    @ApiModelProperty("内容")
    private String text;
    @ApiModelProperty("购买后发送购买者邮箱内容")
    private String emailText;
    @ApiModelProperty("所需积分")
    private long integral;

    public String getHobbyId() {
        return hobbyId;
    }

    public CreateArticleDraftRequest setHobbyId(String hobbyId) {
        this.hobbyId = hobbyId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateArticleDraftRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public CreateArticleDraftRequest setText(String text) {
        this.text = text;
        return this;
    }

    public String getCoverId() {
        return coverId;
    }

    public CreateArticleDraftRequest setCoverId(String coverId) {
        this.coverId = coverId;
        return this;
    }

    public String getInShort() {
        return inShort;
    }

    public CreateArticleDraftRequest setInShort(String inShort) {
        this.inShort = inShort;
        return this;
    }

    public String getEmailText() {
        return emailText;
    }

    public CreateArticleDraftRequest setEmailText(String emailText) {
        this.emailText = emailText;
        return this;
    }

    public long getIntegral() {
        return integral;
    }

    public CreateArticleDraftRequest setIntegral(long integral) {
        this.integral = integral;
        return this;
    }

    public String getId() {
        return id;
    }
}
