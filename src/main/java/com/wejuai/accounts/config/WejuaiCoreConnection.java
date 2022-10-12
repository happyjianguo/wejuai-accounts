package com.wejuai.accounts.config;

import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * @author ZM.Wang
 */
@Profile("prod")
@Configuration
public class WejuaiCoreConnection {

    private final WejuaiCoreClient wejuaiCoreClient;

    public WejuaiCoreConnection(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @PostConstruct
    void originTest( ) {
        wejuaiCoreClient.originTest();
    }
}
