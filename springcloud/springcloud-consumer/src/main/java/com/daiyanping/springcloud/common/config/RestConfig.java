package com.daiyanping.springcloud.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Base64;

@Configuration
public class RestConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        RestTemplate build = builder.build();
        return build;
    }

    @Bean
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders(); // 定义一个HTTP的头信息
        String auth = "admin:test1234"; // 认证的原始信息
        String authString = Base64.getEncoder()
                .encodeToString(auth.getBytes(Charset.forName("UTF-8")));// 进行一个加密的处理
        String authHeader = "Basic " + authString;
        headers.set("Authorization", authHeader);
        return headers;
    }
}
