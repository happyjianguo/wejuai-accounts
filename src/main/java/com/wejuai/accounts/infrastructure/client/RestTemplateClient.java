package com.wejuai.accounts.infrastructure.client;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.wejuai.accounts.config.Constant.MAPPER;

/**
 * @author ZM.Wang
 */
public class RestTemplateClient {

    private final RestTemplate restTemplate;

    public RestTemplateClient() {
        this.restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(MAPPER));
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        this.restTemplate.setInterceptors(interceptors);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
