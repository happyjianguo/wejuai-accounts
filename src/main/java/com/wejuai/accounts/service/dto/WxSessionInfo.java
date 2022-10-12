package com.wejuai.accounts.service.dto;

import java.io.Serializable;

/**
 * @author ZM.Wang
 */
public class WxSessionInfo implements Serializable {

    private String sessionKey;
    private String openId;

    public WxSessionInfo(String sessionKey, String openId) {
        this.sessionKey = sessionKey;
        this.openId = openId;
    }

    WxSessionInfo() {
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getOpenId() {
        return openId;
    }
}
