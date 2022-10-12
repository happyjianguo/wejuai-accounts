package com.wejuai.accounts.web.dto.response;

import com.wejuai.entity.mongo.LoginLog;
import com.wejuai.entity.mysql.Accounts;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

import static com.wejuai.dto.Constant.DATE_FORMAT;

/**
 * @author ZM.Wang
 */
public class AccountInfo {

    @ApiModelProperty("邮箱")
    private final String email;
    @ApiModelProperty("手机")
    private final String phone;
    @ApiModelProperty("最后登录时间")
    private final String lastLoginAt;
    @ApiModelProperty("最后登录ip")
    private final String lastLoginIp;

    public AccountInfo(Accounts accounts, LoginLog loginLog) {
        this.email = accounts.getEmail();
        this.phone = accounts.getPhone();
        this.lastLoginAt = DateFormatUtils.format(loginLog.getCreatedAt(), DATE_FORMAT);
        this.lastLoginIp = loginLog.getIp();
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }
}
