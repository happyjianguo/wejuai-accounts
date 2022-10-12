package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class UpdateCoordinateNameRequest {

    @NotBlank
    @ApiModelProperty("记录id")
    private String id;

    @NotBlank
    @ApiModelProperty("名字")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
