package com.wejuai.accounts.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

public interface Constant {

    ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    String XIAN_LIAO_ID = "13350";
    String XIAN_LIAO_KEY = "ZELYLmwY8vAu4A9Ohy4KStZtDaN4dNNt";

    static String getXlmHash(String uid, long timeMillis) {
        String signStr = XIAN_LIAO_ID + "_" + uid + "_" + timeMillis + "_" + XIAN_LIAO_KEY;
        return DigestUtils.sha512Hex(signStr);
    }

}
