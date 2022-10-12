package com.wejuai.accounts.config;

import com.wejuai.accounts.infrastructure.client.GitlabClient;
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
@EnableConfigurationProperties(GitlabConfig.Properties.class)
public class GitlabConfig {

    public final Properties properties;

    public GitlabConfig(Properties properties) {
        this.properties = properties;
    }

    @Bean
    GitlabClient gitlabClient(RestTemplate restTemplate) {
        return new GitlabClient(restTemplate, properties.getId(), properties.getKey());
    }

    @Validated
    @ConfigurationProperties(prefix = "gitlab")
    public static class Properties {
        @NotBlank
        private String id;

        private String key;

        public String getId() {
            return id;
        }

        public Properties setId(String id) {
            this.id = id;
            return this;
        }

        public String getKey() {
            return key;
        }

        public Properties setKey(String key) {
            this.key = key;
            return this;
        }
    }
}
