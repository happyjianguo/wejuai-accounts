package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class CreateArticleRequest {

    @ApiModelProperty("如果是修改传文章id")
    private String id;
    @NotBlank(message = "爱好是必选的哦~")
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
    @Length(max = 1000, message = "最多只能发送1000个字哦")
    private String emailText;
    @ApiModelProperty("所需积分")
    private long integral;

    public String getId() {
        return id;
    }

    public CreateArticleRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getHobbyId() {
        return hobbyId;
    }

    public CreateArticleRequest setHobbyId(String hobbyId) {
        this.hobbyId = hobbyId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateArticleRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public CreateArticleRequest setText(String text) {
        this.text = text;
        return this;
    }

    public String getCoverId() {
        return coverId;
    }

    public CreateArticleRequest setCoverId(String coverId) {
        this.coverId = coverId;
        return this;
    }

    public String getInShort() {
        return inShort;
    }

    public CreateArticleRequest setInShort(String inShort) {
        this.inShort = inShort;
        return this;
    }

    public long getIntegral() {
        return integral;
    }

    public CreateArticleRequest setIntegral(long integral) {
        this.integral = integral;
        return this;
    }

    public String getEmailText() {
        return emailText;
    }

    public CreateArticleRequest setEmailText(String emailText) {
        this.emailText = emailText;
        return this;
    }
}
