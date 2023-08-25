package com.nan.jvm.chatConfig;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptClientConfig {

    private final static String PROXY_HOST="127.0.0.1";
    private final static int PROXY_PORT=7890;

    @Bean(name = "ChatGptHttpClient")
    public CloseableHttpClient GetHttpClient(){

        HttpHost ProxyHost = new HttpHost(PROXY_HOST, PROXY_PORT);
        CloseableHttpClient closeableHttpClient = HttpClients.custom().setRoutePlanner(new DefaultProxyRoutePlanner(ProxyHost)).build();


        return closeableHttpClient;
    }
}
