package com.wejuai.accounts.web.dto.request;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class WxUserInfoRequest {

    private String nickName;
    private String avatarUrl;
    private int gender;
    private String country;
    private String province;
    private String city;

    private String rawData;
    private String signature;
    @NotBlank
    private String encryptedData;
    @NotBlank
    private String iv;


    public String getNickName() {
        return nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getSignature() {
        return signature;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public String getRawData() {
        return rawData;
    }
}
