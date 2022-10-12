package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
public class UpdateRewardSubmissionDraftRequest {

    @ApiModelProperty("选定结果前展示内容")
    private String inShort;

    @ApiModelProperty("选定后展示内容内容，富文本")
    @NotBlank(message = "提交内容必填")
    private String text;

    public String getInShort() {
        return inShort;
    }

    public String getText() {
        return text;
    }
}
