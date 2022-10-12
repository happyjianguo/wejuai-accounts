package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * Created by ZM.Wang
 */
public class UpdateUserHeadImageRequest {

    @NotBlank(message = "ossKey不允许为空")
    @ApiModelProperty("图片上传到oss后的key")
    private String ossKey;

    public String getOssKey() {
        return ossKey;
    }
}
