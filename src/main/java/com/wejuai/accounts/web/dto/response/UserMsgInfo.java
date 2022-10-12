package com.wejuai.accounts.web.dto.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class UserMsgInfo {

    @ApiModelProperty("聊天未读消息数量")
    private long sendMsgUnreadNum;

    @ApiModelProperty("系统未读消息数量")
    private long systemMsgUnreadNum;

    public UserMsgInfo(long sendMsgUnreadNum, long systemMsgUnreadNum) {
        this.sendMsgUnreadNum = sendMsgUnreadNum;
        this.systemMsgUnreadNum = systemMsgUnreadNum;
    }

    UserMsgInfo() {
    }

    public long getSendMsgUnreadNum() {
        return sendMsgUnreadNum;
    }

    public long getSystemMsgUnreadNum() {
        return systemMsgUnreadNum;
    }
}
