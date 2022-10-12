package com.wejuai.accounts.infrastructure.mail;

import com.wejuai.accounts.domain.EmailCode;

/**
 * 邮差
 *
 * @author YQ.Huang
 */
public interface MailPoster {

    void sendRegistrationCode(EmailCode emailCode);

    void sendPasswordResetMail(EmailCode emailCode);

    void sendEditEmailMail(EmailCode emailCode);
}
