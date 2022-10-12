package com.wejuai.accounts.web.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class CreateEmailCodeRequest {

    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱为空")
    private String email;

    public String getEmail() {
        return email;
    }
}
