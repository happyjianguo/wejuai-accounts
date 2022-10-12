package com.wejuai.accounts.config;

import com.endofmaster.weixin.basic.WxBasicApi;
import com.endofmaster.weixin.qr.code.WxQrCodeApi;
import com.endofmaster.weixin.support.WxHttpClient;
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
@EnableConfigurationProperties(WeixinConfig.Properties.class)
public class WeixinConfig {

    private final Properties weixin;

    public WeixinConfig(Properties weixin) {
        this.weixin = weixin;
    }

    @Bean
    WxHttpClient wxHttpClient() {
        return new WxHttpClient();
    }

    /** 代表开放平台 */
    @Bean
    WxBasicApi wxOpenBasicApi(WxHttpClient wxHttpClient) {
        return new WxBasicApi(weixin.getOpenId(), weixin.getOpenSecret(), wxHttpClient);
    }

    /** 代表公众平台 */
    @Bean
    WxBasicApi wxOffiBasicApi(WxHttpClient wxHttpClient) {
        return new WxBasicApi(weixin.getOffiId(), weixin.getOffiSecret(), wxHttpClient);
    }

    /** 代表小程序 */
    @Bean
    WxBasicApi wxAppBasicApi(WxHttpClient wxHttpClient) {
        return new WxBasicApi(weixin.getAppId(), weixin.getAppSecret(), wxHttpClient);
    }

    @Bean
    WxQrCodeApi wxQrCodeApi() {
        return new WxQrCodeApi();
    }

    @Validated
    @ConfigurationProperties(prefix = "weixin")
    public static class Properties {

        @NotBlank
        private String openId;

        @NotBlank
        private String openSecret;

        @NotBlank
        private String appId;

        @NotBlank
        private String appSecret;

        @NotBlank
        private String offiId;

        @NotBlank
        private String offiSecret;

        @NotBlank
        private String redirectUrl;

        public String getOpenId() {
            return openId;
        }

        public String getOpenSecret() {
            return openSecret;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public Properties setOpenId(String openId) {
            this.openId = openId;
            return this;
        }

        public Properties setOpenSecret(String openSecret) {
            this.openSecret = openSecret;
            return this;
        }

        public Properties setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public String getAppId() {
            return appId;
        }

        public Properties setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public Properties setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        public String getOffiId() {
            return offiId;
        }

        public Properties setOffiId(String offiId) {
            this.offiId = offiId;
            return this;
        }

        public String getOffiSecret() {
            return offiSecret;
        }

        public Properties setOffiSecret(String offiSecret) {
            this.offiSecret = offiSecret;
            return this;
        }
    }

    public Properties getWeixin() {
        return weixin;
    }

    public String getOpenSecret() {
        return weixin.getOpenSecret();
    }

    public String getAppSecret() {
        return weixin.getAppSecret();
    }

    public String getOffiSecret() {
        return weixin.getOffiSecret();
    }

    public String getRedirectUrl() {
        return weixin.getRedirectUrl();
    }

    public String getAppId() {
        return weixin.getAppId();
    }

}
