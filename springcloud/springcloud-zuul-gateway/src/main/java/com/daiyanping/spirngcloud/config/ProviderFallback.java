package com.daiyanping.spirngcloud.config;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName ProviderFallback
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-24
 * @Version 0.1
 */
//zuul 本身进行服务降级
@Component
public class ProviderFallback implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {


            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders() ;
                headers.set("Content-Type", "text/html; charset=UTF-8");
                return headers;
            }

            @Override
            public InputStream getBody() throws IOException {
//                 响应体
                return new ByteArrayInputStream("产品微服务不可用，请稍后再试。".getBytes());
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.BAD_REQUEST;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.BAD_REQUEST.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.BAD_REQUEST.getReasonPhrase();
            }

            @Override
            public void close() {

            }
        };
    }
}
