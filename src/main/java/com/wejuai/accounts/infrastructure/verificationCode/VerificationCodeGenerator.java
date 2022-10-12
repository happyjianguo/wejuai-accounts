package com.wejuai.accounts.infrastructure.verificationCode;

import com.wejuai.accounts.domain.EmailCode;
import com.wejuai.accounts.domain.EmailType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.util.Date;

/**
 * 验证码生成器
 *
 * @author YQ.Huang
 */
public class VerificationCodeGenerator {

    private int maxAgeSeconds;
    private int numberCount;

    /**
     * 构造
     *
     * @param maxAgeSeconds 最大有效期，默认86400秒（24小时）
     * @param numberCount   数字位数，默认6位
     */
    public VerificationCodeGenerator(int maxAgeSeconds, int numberCount) {
        Validate.isTrue(maxAgeSeconds > 0);
        Validate.isTrue(numberCount > 0);
        this.maxAgeSeconds = maxAgeSeconds;
        this.numberCount = numberCount;
    }

    public EmailCode getSignUpEmailCode(String email) {
        String code = RandomStringUtils.randomNumeric(numberCount);
        Date expiredAt = Date.from(Instant.now().plusSeconds(maxAgeSeconds));
        return new EmailCode(email, code, expiredAt, EmailType.SIGN_UP);
    }

    public EmailCode getPasswordResetEmailCode(String email) {
        String code = RandomStringUtils.randomNumeric(numberCount);
        Date expiredAt = Date.from(Instant.now().plusSeconds(maxAgeSeconds));
        return new EmailCode(email, code, expiredAt, EmailType.PASSWORD_RESET);
    }

    public EmailCode getEditEmailEmailCode(String email, String userId) {
        String code = RandomStringUtils.randomNumeric(numberCount);
        Date expiredAt = Date.from(Instant.now().plusSeconds(maxAgeSeconds));
        return new EmailCode(email, code, expiredAt, userId);
    }

}
