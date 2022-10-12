package com.wejuai.accounts.config;

import com.wejuai.accounts.infrastructure.client.TradeGatewayClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
@Configuration
@EnableConfigurationProperties(TradeConfig.Properties.class)
public class TradeConfig {

    public final Properties properties;

    public TradeConfig(Properties properties) {
        this.properties = properties;
    }

    @Bean
    TradeGatewayClient tradeGatewayClient(RestTemplate restTemplate) {
        return new TradeGatewayClient(restTemplate, properties.getUrl());
    }

    @Validated
    @ConfigurationProperties(prefix = "trade-gateway")
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
