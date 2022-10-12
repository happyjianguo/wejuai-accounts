package com.wejuai.accounts.web.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class VerifyEmailCodeRequest {

    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱为空")
    private String email;
    @NotBlank(message = "邮箱验证码为空")
    private String code;
    @NotBlank(message = "设置密码为空")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }
}
