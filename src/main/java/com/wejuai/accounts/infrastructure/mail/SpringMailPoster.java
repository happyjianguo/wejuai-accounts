package com.wejuai.accounts.infrastructure.mail;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.accounts.domain.EmailCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import static org.springframework.data.redis.core.convert.Bucket.CHARSET;


/**
 * 基于Spring Mail的MailPoster实现
 *
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class SpringMailPoster implements MailPoster {

    private final static Logger logger = LoggerFactory.getLogger(SpringMailPoster.class);

    private final JavaMailSender mailSender;
    private final String mail;
    private final boolean enabled;
    private final String emailHtml;

    public SpringMailPoster(JavaMailSender mailSender, String mail, boolean enabled) throws IOException {
        this.mailSender = mailSender;
        this.mail = mail;
        this.enabled = enabled;
        this.emailHtml = StreamUtils.copyToString(this.getClass().getResourceAsStream("/emailCode.html"), CHARSET);
    }

    @Override
    public void sendRegistrationCode(EmailCode emailCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setSentDate(new Date());
            message.setFrom(mail, "为聚爱");
            message.setTo(emailCode.getEmail());
            message.setSubject("【为聚爱】欢迎注册");
            String text = emailHtml.replace("xxxxxx", emailCode.getCode());
            text = text.replace("xxxx", emailCode.getEmail());
            message.setText(text, true);
            if (enabled) {
                mailSender.send(mimeMessage);
            } else {
                logger.info("注册邮件:\n\nto={}\ncode={}\nexpiredAt={}\n\n",
                        emailCode.getEmail(),
                        emailCode.getCode(),
                        emailCode.getExpiredAt());
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ServerException("发送邮件错误", e);
        }
    }

    @Override
    public void sendPasswordResetMail(EmailCode emailCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setSentDate(new Date());
            message.setFrom(mail, "为聚爱");
            message.setTo(emailCode.getEmail());
            message.setSubject("【为聚爱】密码重置");
            String text = emailHtml.replace("xxxxxx", emailCode.getCode());
            text = text.replace("xxxx", emailCode.getEmail());
            message.setText(text, true);
            if (enabled) {
                mailSender.send(mimeMessage);
            } else {
                logger.info("密码重置邮件:\n\nto={}\ncode={}\nexpiredAt={}\n\n",
                        emailCode.getEmail(),
                        emailCode.getCode(),
                        emailCode.getExpiredAt());
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ServerException("发送邮件错误", e);
        }
    }

    @Override
    public void sendEditEmailMail(EmailCode emailCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setSentDate(new Date());
            message.setFrom(mail, "为聚爱");
            message.setTo(emailCode.getEmail());
            message.setSubject("【为聚爱】修改邮箱");
            String text = emailHtml.replace("xxxxxx", emailCode.getCode());
            text = text.replace("xxxx", emailCode.getEmail());
            message.setText(text, true);
            if (enabled) {
                mailSender.send(mimeMessage);
            } else {
                logger.info("修改邮箱:\n\nto={}\ncode={}\nexpiredAt={}\n\n",
                        emailCode.getEmail(),
                        emailCode.getCode(),
                        emailCode.getExpiredAt());
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ServerException("发送邮件错误", e);
        }
    }
}
