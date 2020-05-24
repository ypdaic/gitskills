package com.daiyanping.springcloud.common.oauth2;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.List;

/*
  鉴权过滤器
* @  OAuth2AuthenticationProcessingFilter
*
* */
@EnableOAuth2Client
@EnableConfigurationProperties
@Configuration
public class OAuth2ClientConfig {


    /**
     * 客户端模式的资源配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /**
     * fegin 的支持
     * @return
     */
//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor(ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails);
//    }

    @Autowired
    RetryLoadBalancerInterceptor loadBalancerInterceptor;

//    /**
//     * 用于拦截器调用oauth2 认证服务器接口，这种配置是无效的
//     * @return
//     */
//    @Bean
//    public OAuth2RestTemplate clientCredentialsRestTemplate() {
//
//        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails());
//        List<ClientHttpRequestInterceptor> interceptors = oAuth2RestTemplate.getInterceptors();
//        interceptors.add(loadBalancerInterceptor);
//        oAuth2RestTemplate.setInterceptors(interceptors);
//        return oAuth2RestTemplate;
//    }

    // 支持服务端的集群
    @Bean
    public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
        return new MyUserInfoRestTemplateCustomizer(loadBalancerInterceptor);
    }

    @AllArgsConstructor
    public static class MyUserInfoRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {

        private RetryLoadBalancerInterceptor loadBalancerInterceptor;

        @Override
        public void customize(OAuth2RestTemplate template) {
            List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
            interceptors.add(loadBalancerInterceptor);
            template.setInterceptors(interceptors);
        }
    }
}
