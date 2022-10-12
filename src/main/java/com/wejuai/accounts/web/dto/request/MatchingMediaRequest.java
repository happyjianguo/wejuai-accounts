package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 * 补全媒体信息dto
 */
public class MatchingMediaRequest {

    @NotBlank
    @ApiModelProperty("拼接好的key或者接口返回的key")
    private String ossKey;

    public String getOssKey() {
        return ossKey;
    }

    public MatchingMediaRequest setOssKey(String ossKey) {
        this.ossKey = ossKey;
        return this;
    }
}
