package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author ZM.Wang
 */
public class CreateHobbyApplyRequest {

    @NotBlank(message = "最少也要写十个字哦~")
    @Size(min = 10, message = "最少也要写十个字哦~")
    @ApiModelProperty("内容")
    private String text;

    @NotBlank(message = "联系方式必须填哦~")
    @ApiModelProperty("联系方式")
    private String contact;

    public String getText() {
        return text;
    }

    public CreateHobbyApplyRequest setText(String text) {
        this.text = text;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public CreateHobbyApplyRequest setContact(String contact) {
        this.contact = contact;
        return this;
    }
}
