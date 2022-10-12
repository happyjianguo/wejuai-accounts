package com.wejuai.accounts.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author ZM.Wang
 */
@Entity
public class EmailCode {

    @Id
    @Column(length = 32)
    private String email;
    private String code;
    private Date updateAt;
    private Date expiredAt;
    private String userId;
    private EmailType type;

    public EmailCode(String email, String code, Date expiredAt, EmailType type) {
        this.email = email;
        this.code = code;
        this.updateAt = new Date();
        this.expiredAt = expiredAt;
        this.type = type;
    }

    public EmailCode(String email, String code, Date expiredAt, String userId) {
        this.email = email;
        this.code = code;
        this.updateAt = new Date();
        this.expiredAt = expiredAt;
        this.userId = userId;
        this.type = EmailType.EDIT_EMAIL;
    }

    EmailCode() {
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public String getUserId() {
        return userId;
    }

    public EmailType getType() {
        return type;
    }
}
