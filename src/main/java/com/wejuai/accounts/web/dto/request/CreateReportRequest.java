package com.wejuai.accounts.web.dto.request;

import com.wejuai.entity.mongo.ReportType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ZM.Wang
 */
public class CreateReportRequest {

    @NotNull
    @ApiModelProperty("举报类型")
    private ReportType type;

    @ApiModelProperty("被举报用户的userId")
    private String beUserId;

    @ApiModelProperty("应用id")
    private String appId;

    @ApiModelProperty("应用类型")
    private ReportAppType appType;

    @NotBlank(message = "举报原因必填")
    @Length(min = 20, max = 200, message = "举报原因字数必须在20到200之间")
    @ApiModelProperty("举报原因")
    private String reason;

    public String getAppId() {
        return appId;
    }

    public ReportAppType getAppType() {
        return appType;
    }

    public String getReason() {
        return reason;
    }

    public String getBeUserId() {
        return beUserId;
    }

    public ReportType getType() {
        return type;
    }
}
