package com.daiyanping.springcloud.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
//启用全局方法安全注解，就可以在方法上使用注解来对请求进行过滤
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

//    @Autowired
//    private TokenStore tokenStore;

    /* 针对@EnableGlobalMethodSecurity(prePostEnabled = true)
   *   access(String) 如果给定的SpEL表达式计算结果为true，就允许访问
       anonymous() 允许匿名用户访问
       authenticated() 允许认证的用户进行访问
       denyAll() 无条件拒绝所有访问
       fullyAuthenticated() 如果用户是完整认证的话（不是通过Remember-me功能认证的），就允许访问
       hasAuthority(String) 如果用户具备给定权限的话就允许访问
       hasAnyAuthority(String…)如果用户具备给定权限中的某一个的话，就允许访问
       hasRole(String) 如果用户具备给定角色(用户组)的话,就允许访问/
       hasAnyRole(String…) 如果用户具有给定角色(用户组)中的一个的话,允许访问.
       hasIpAddress(String 如果请求来自给定ip地址的话,就允许访问.
       not() 对其他访问结果求反.
       permitAll() 无条件允许访问
       rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
   *
   * */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // 配置order访问控制，必须认证后才可以访问，资源不在该配置中，则不受拦截
        http.authorizeRequests()
                .antMatchers("/order/**","/user/**", "/product/**").authenticated();
    }

    /*
    * 把token验证失败后，重新刷新token的类设置到 OAuth2AuthenticationProcessingFilter
    * token验证过滤器中
    * */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("micro-web").resourceId("client_2");
        super.configure(resources);
//        resources.authenticationEntryPoint(new RefreshTokenAuthenticationEntryPoint());
//        resources.tokenStore(tokenStore);
    }
}
