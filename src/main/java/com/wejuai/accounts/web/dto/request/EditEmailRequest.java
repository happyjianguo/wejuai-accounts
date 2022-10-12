package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class EditEmailRequest {

    @NotBlank(message = "旧邮箱验证码必填")
    @ApiModelProperty("旧邮箱验证码")
    private String oldEmailCode;

    @NotBlank(message = "新邮箱必填")
    @ApiModelProperty("新邮箱")
    private String newEmail;

    @NotBlank(message = "新邮箱验证码必填")
    @ApiModelProperty("新邮箱验证码")
    private String newEmailCode;

    public String getOldEmailCode() {
        return oldEmailCode;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public String getNewEmailCode() {
        return newEmailCode;
    }
}
