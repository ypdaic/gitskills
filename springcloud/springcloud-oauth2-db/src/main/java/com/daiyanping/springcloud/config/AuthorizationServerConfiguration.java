package com.daiyanping.springcloud.config;

import com.daiyanping.springcloud.service.UserServiceDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenStore tokenStore;

    /**
     * 密码模式下的用户信息
     */
    @Autowired
    private UserServiceDetail userServiceDetail;

    static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);

    /**
     * jdbc形式存储token信息
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 使用jdbc存储授权模式下的code
     * @return
     */
    @Bean
    public JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 使用jdbc存储Approval，也就是存储用户同意授权的过期时间，及授权状态
     */
    public JdbcApprovalStore jdbcApprovalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * jdbc 形式存储客户端信息，客户端信息可以是如下几种
     *
     *         1.授权码模式（authorization code）
     *         2.简化模式（implicit）
     *         3.密码模式（resource owner password credentials）
     *         4.客户端模式（client credentials）
     * @return
     */
    @Bean // 声明 ClientDetails实现
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // redisTokenStore
//        endpoints.tokenStore(new MyRedisTokenStore(redisConnectionFactory))
//                .authenticationManager(authenticationManager)
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        // 1声明token存储方式
        // 2定义了权限的校验的管理器
        // 3定义了用户校验service
        // 4定义token属性信息
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userServiceDetail)
                // 运行GET，post的方式获取token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        // 配置tokenServices参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        //支持refreshtoken
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        // token 过期时间
        tokenServices.setAccessTokenValiditySeconds(60 * 5);
        //重复使用
        tokenServices.setReuseRefreshToken(false);
        // 刷新过期时间
        tokenServices.setRefreshTokenValiditySeconds(60 * 10);
        // 设置token服务实例类，不设置默认也会提供
        endpoints.tokenServices(tokenServices);
        // 配置授权code的存储方式
        endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices());
        // 配置token生成器，默认已经配置了5种，无需我们再进行配置
        endpoints.tokenGranter(null);
        // 配置Approval存储方式
        endpoints.approvalStore(jdbcApprovalStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

}
