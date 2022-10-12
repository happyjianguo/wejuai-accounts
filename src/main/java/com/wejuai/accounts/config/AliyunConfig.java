package com.wejuai.accounts.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.endofmaster.commons.aliyun.oss.AliyunOss;
import com.wejuai.accounts.infrastructure.mns.AliyunMnsTradeEventPublisher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author YQ.Huang
 */
@Configuration
@EnableConfigurationProperties({
        AliyunConfig.Properties.class,
        AliyunConfig.MnsProperties.class,
        AliyunConfig.OssProperties.class})
public class AliyunConfig {

    public final Properties aliyun;
    public final OssProperties oss;
    public final MnsProperties mns;

    public AliyunConfig(Properties aliyun, OssProperties oss, MnsProperties mns) {
        this.aliyun = aliyun;
        this.oss = oss;
        this.mns = mns;
    }

    @Bean
    AliyunOss aliyunOss() {
        return new AliyunOss(oss.getBucket(), oss.getEndpoint(), aliyun.getAccessKeyId(), aliyun.getAccessKeySecret());
    }

    @Bean
    AliyunMnsTradeEventPublisher aliyunMnsTradeEventPublisher() {
        CloudAccount cloudAccount = new CloudAccount(aliyun.getAccessKeyId(), aliyun.getAccessKeySecret(), mns.getEndpoint());
        MNSClient mnsClient = cloudAccount.getMNSClient();
        CloudQueue chargeStartedQueue = mnsClient.getQueueRef(mns.getName());
        return new AliyunMnsTradeEventPublisher(chargeStartedQueue);
    }

    @Validated
    @ConfigurationProperties(prefix = "aliyun")
    public static class Properties {

        @NotBlank
        private String accessKeyId;
        @NotBlank
        private String accessKeySecret;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
    }

    @Validated
    @ConfigurationProperties(prefix = "aliyun.oss")
    public static class OssProperties {

        @NotBlank
        private String bucket;

        @NotBlank
        private String endpoint;

        @NotBlank
        private String imageCallbackUrl;

        @NotBlank
        private String videoCallbackUrl;

        @NotBlank
        private String audioCallbackUrl;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getImageCallbackUrl() {
            return imageCallbackUrl;
        }

        public OssProperties setImageCallbackUrl(String imageCallbackUrl) {
            this.imageCallbackUrl = imageCallbackUrl;
            return this;
        }

        public String getVideoCallbackUrl() {
            return videoCallbackUrl;
        }

        public OssProperties setVideoCallbackUrl(String videoCallbackUrl) {
            this.videoCallbackUrl = videoCallbackUrl;
            return this;
        }

        public String getAudioCallbackUrl() {
            return audioCallbackUrl;
        }

        public OssProperties setAudioCallbackUrl(String audioCallbackUrl) {
            this.audioCallbackUrl = audioCallbackUrl;
            return this;
        }
    }

    @Validated
    @ConfigurationProperties(prefix = "aliyun.mns")
    public static class MnsProperties {
        @NotBlank
        private String name;
        @NotBlank
        private String endpoint;

        public String getName() {
            return name;
        }

        public MnsProperties setName(String name) {
            this.name = name;
            return this;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public MnsProperties setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
    }
}