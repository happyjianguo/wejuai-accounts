package com.wejuai.accounts.infrastructure.client;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.accounts.web.dto.request.PushWebLogRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author ZM.Wang
 */
public class GitlabClient {

    private final RestTemplate restTemplate;
    private final String id;
    private final static String url = "https://gitlab.com";

    public GitlabClient(RestTemplate restTemplate, String id, String privateKey) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new HeaderRequestInterceptor("PRIVATE-TOKEN", privateKey));
        restTemplate.setInterceptors(interceptors);
        this.restTemplate = restTemplate;
        this.id = id;
    }

    public void pushNewFile(String filePath, PushWebLogRequest request) {
        try {
            restTemplate.postForObject(url + "/api/v4/projects/{id}/repository/files/{filePath}",
                    request, Void.class, id, filePath);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }
}
