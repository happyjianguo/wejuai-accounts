package com.wejuai.accounts.web.dto.request;

import com.wejuai.dto.request.ApplyCancelRewardDemandRequest;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class CreateApplyCancelRewardDemandRequest {

    @ApiModelProperty("悬赏id")
    @NotBlank(message = "悬赏id必填")
    private String id;

    @ApiModelProperty("原因")
    @NotBlank(message = "原因必填")
    @Length(min = 20, max = 200, message = "申请原因字数在20到200之间")
    private String reason;

    public ApplyCancelRewardDemandRequest getCoreReq(User user) {
        return new ApplyCancelRewardDemandRequest(id, user.getId(), reason);
    }

    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }
}
