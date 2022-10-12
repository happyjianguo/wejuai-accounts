package com.wejuai.accounts.config;

import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
@Order(1)
@Configuration
@EnableConfigurationProperties(WejuaiCoreConfig.Properties.class)
public class WejuaiCoreConfig {

    private final Properties properties;

    public WejuaiCoreConfig(Properties properties) {
        this.properties = properties;
    }

    @Bean
    WejuaiCoreClient wejuaiCoreClient(RestTemplate restTemplate) {
        return new WejuaiCoreClient(restTemplate, properties.getUrl());
    }

    @Validated
    @ConfigurationProperties(prefix = "wejuai-core")
    public static class Properties {
        @NotBlank
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
