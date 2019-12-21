package com.daiyanping.springcloud.common.config;

import com.daiyanping.ribbon.RibbonConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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

    /**
     * 随机路由，此处配置的是全局的，所有的服务都是使用该规则,虽然在RibbonClientConfiguration
     * 自动配置了IRule，但是会去父容器中查找是否存在，如果不存在才会自动注入
     * @return
     */
    @Bean
    @Lazy
    public IRule ribbonRule() { // 其中IRule就是所有规则的标准
        return new com.netflix.loadbalancer.RandomRule(); // 随机的访问策略
    }
}
