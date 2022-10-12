package com.wejuai.accounts.config;

import com.endofmaster.weibo.WeiboHttpClient;
import com.endofmaster.weibo.basic.WeiboBasicApi;
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
@EnableConfigurationProperties(WeiboConfig.Properties.class)
public class WeiboConfig {

    public final WeiboConfig.Properties weibo;

    public WeiboConfig(Properties weibo) {
        this.weibo = weibo;
    }

    @Bean
    WeiboBasicApi weiboBasicApi() {
        return new WeiboBasicApi(weibo.getAppId(), weibo.getAppKey(), weibo.getRedirectUrl(), new WeiboHttpClient());
    }

    @Validated
    @ConfigurationProperties(prefix = "weibo")
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
