package com.wejuai.accounts.web.dto.response;

import com.wejuai.accounts.config.Constant;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class TimInfoResponse {

    @ApiModelProperty("时间戳")
    private final String timestamp;

    @ApiModelProperty("用户聊天id")
    private final String uid;

    @ApiModelProperty("加密串")
    private final String xlmHash;

    public TimInfoResponse(long timestamp, String uid) {
        this.timestamp = timestamp + "";
        this.uid = uid;
        this.xlmHash = Constant.getXlmHash(uid, timestamp);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getXlmHash() {
        return xlmHash;
    }
}
