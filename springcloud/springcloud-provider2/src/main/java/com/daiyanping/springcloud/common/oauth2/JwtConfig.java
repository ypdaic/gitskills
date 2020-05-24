package com.daiyanping.springcloud.common.oauth2;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterRestTemplateCustomizer;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class JwtConfig {

//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//    /**
//     * 客户端配置tokenStore后，无需再向服务端发送请求校验token，获取用户信息
//     * @return
//     */
//    @Bean
//    @Qualifier("tokenStore")
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter);
//    }
//
//    @Bean
//    public JwtAccessTokenConverter jwtTokenEnhancer() {
//        // 配置jks文件
////        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("micro-jwt.jks"), "123456".toCharArray());
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
////        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("micro-jwt"));
//        converter.setSigningKey("kSUdVKL0j0JGTAIo8uY5ZNMO9nZAemg6ehgOHozK");
//        return converter;
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
    public JwtAccessTokenConverterRestTemplateCustomizer jwtAccessTokenConverterRestTemplateCustomizer() {
        return new MyJwtAccessTokenConverterRestTemplateCustomizer(loadBalancerInterceptor);
    }

    @AllArgsConstructor
    public static class MyJwtAccessTokenConverterRestTemplateCustomizer implements JwtAccessTokenConverterRestTemplateCustomizer {

        private RetryLoadBalancerInterceptor loadBalancerInterceptor;


        @Override
        public void customize(RestTemplate template) {
            List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
            interceptors.add(loadBalancerInterceptor);
            template.setInterceptors(interceptors);
        }
    }
}
