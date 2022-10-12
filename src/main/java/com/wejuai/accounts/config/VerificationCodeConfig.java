package com.wejuai.accounts.config;

import com.wejuai.accounts.infrastructure.verificationCode.VerificationCodeGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author ZM.Wang
 */
@Configuration
@EnableConfigurationProperties(VerificationCodeConfig.Properties.class)
public class VerificationCodeConfig {

    public final Properties verificationCode;

    public VerificationCodeConfig(Properties verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Bean
    VerificationCodeGenerator verificationCodeGenerator() {
        return new VerificationCodeGenerator(verificationCode.getMaxAgeSeconds(), verificationCode.getNumberCount());
    }

    @Validated
    @ConfigurationProperties(prefix = "verification.code")
    public static class Properties {

        private int numberCount = 6;

        private int maxAgeSeconds = 15 * 60;

        public int getNumberCount() {
            return numberCount;
        }

        public Properties setNumberCount(int numberCount) {
            this.numberCount = numberCount;
            return this;
        }

        public int getMaxAgeSeconds() {
            return maxAgeSeconds;
        }

        public Properties setMaxAgeSeconds(int maxAgeSeconds) {
            this.maxAgeSeconds = maxAgeSeconds;
            return this;
        }
    }
}
