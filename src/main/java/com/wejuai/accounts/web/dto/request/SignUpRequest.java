package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author ZM.Wang
 */
public class SignUpRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty("邮箱")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("密码")
    @Pattern(regexp = "[\\w\\.!@#\\$%\\^\\&\\*\\(\\)-_=\\+\\[\\]\\{\\}`,\\/\\<\\>\\?~]{7,16}", message = "请输入正确的密码格式")
    private String password;

    public String getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
