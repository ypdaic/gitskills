package com.daiyanping.springcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 开启认证服务器注解
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public TokenStore tokenStore() {
        return new MyRedisTokenStore(connectionFactory);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // AuthorizationServerEndpointsConfigurer 默认会
        endpoints
                .authenticationManager(authenticationManager)
                // 密码模式有用，密码模式下就和用户绑定了
                .userDetailsService(userDetailsService)//若无，refresh_token会有UserDetailsService is required错误
                // 定义token保存方式
                .tokenStore(tokenStore()).
                allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /*
    *   1.授权码模式（authorization code）
        2.简化模式（implicit）
        3.密码模式（resource owner password credentials）
        4.客户端模式（client credentials）
    * */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");

        // 客户端的基本信息
        clients.inMemory().withClient("micro-web")
                .resourceIds("micro-web")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("all","read", "write","aa")
                .authorities("client_credentials")
                .secret(finalSecret)
                // 设置token有效期
                .accessTokenValiditySeconds(1200)
                .refreshTokenValiditySeconds(50000)
                .and()
                // 密码模式，密码模式下，需要在header中使用base64 将这里的clientId，password传递过来
                .withClient("client_2")
                .resourceIds("client_2")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("server")
                .authorities("oauth2")
                .secret(finalSecret)
                .accessTokenValiditySeconds(1200)
                .refreshTokenValiditySeconds(50000);
    }

    public static void main(String[] args) {
        String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
        System.out.println(finalSecret);
    }

}
