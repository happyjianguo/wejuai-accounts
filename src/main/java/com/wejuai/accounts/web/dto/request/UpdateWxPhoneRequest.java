package com.wejuai.accounts.web.dto.request;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class UpdateWxPhoneRequest {

    @NotBlank
    private String encryptedData;

    @NotBlank
    private String iv;

    private String code;

    public String getEncryptedData() {
        return encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public String getCode() {
        return code;
    }
}
