package com.wejuai.accounts.config;

import com.endofmaster.qq.QqHttpClient;
import com.endofmaster.qq.basic.QqBasicApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
@Configuration
@EnableConfigurationProperties(QqConfig.Properties.class)
public class QqConfig {

    public final QqConfig.Properties qq;

    public QqConfig(Properties qq) {
        this.qq = qq;
    }

    @Bean
    QqBasicApi qqBasicApi() {
        return new QqBasicApi(qq.getAppId(), qq.getAppKey(), qq.getRedirectUrl(), new QqHttpClient());
    }

    @Validated
    @ConfigurationProperties(prefix = "qq")
    public static class Properties {

        @NotBlank
        private String appId;
        @NotBlank
        private String appKey;
        @NotBlank
        private String redirectUrl;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public Properties setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }
    }
}
