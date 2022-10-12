package com.wejuai.accounts.infrastructure.client;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.dto.request.RechargeRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author ZM.Wang
 */
public class TradeGatewayClient {

    private final RestTemplate restTemplate;
    private final String url;

    public TradeGatewayClient(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Map<String, String> payment(String userId, RechargeRequest rechargeRequest, String ip) {
        try {
            return restTemplate.exchange(url + "/trade/{userId}?ip={ip}", HttpMethod.POST, new HttpEntity<>(rechargeRequest),
                    new ParameterizedTypeReference<Map<String, String>>() {
                    }, userId, ip).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

}
