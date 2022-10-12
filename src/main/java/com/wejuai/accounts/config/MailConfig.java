package com.wejuai.accounts.config;

import com.wejuai.accounts.infrastructure.mail.MailPoster;
import com.wejuai.accounts.infrastructure.mail.SpringMailPoster;
import com.wejuai.accounts.domain.EmailCode;
import com.wejuai.accounts.repository.EmailCodeRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Date;

/**
 * @author YQ.Huang
 */
@Configuration
@EnableConfigurationProperties({MailConfig.Properties.class})
public class MailConfig {

    public final Properties mail;

    public MailConfig(Properties mail) {
        this.mail = mail;
    }

    @Bean
    MailPoster mailPoster(JavaMailSender javaMailSender) throws IOException {
        return new SpringMailPoster(javaMailSender, mail.getUsername(), mail.getEnabled());
    }

    @Validated
    @ConfigurationProperties(prefix = "mail")
    public static class Properties {

        private boolean enabled = true;

        @NotBlank
        private String username;

        @NotBlank
        private String password;

        public boolean getEnabled() {
            return enabled;
        }

        public Properties setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Properties setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Properties setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
