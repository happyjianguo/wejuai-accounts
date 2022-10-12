package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author ZM.Wang
 */
public class CreateFeedbackRequest {

    @NotBlank
    @Size(min = 10, message = "最少也要写十个字哦~")
    @ApiModelProperty("内容")
    private String text;
    @ApiModelProperty("联系方式")
    private String contact;

    public String getText() {
        return text;
    }

    public CreateFeedbackRequest setText(String text) {
        this.text = text;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public CreateFeedbackRequest setContact(String contact) {
        this.contact = contact;
        return this;
    }
}
