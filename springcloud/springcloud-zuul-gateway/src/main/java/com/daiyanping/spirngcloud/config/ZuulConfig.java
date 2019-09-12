package com.daiyanping.spirngcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ZuulConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-12
 * @Version 0.1
 */
@Configuration
public class ZuulConfig {
    @Bean
    public AuthorizedRequestFilter getAuthorizedRequestFilter() {
        return new AuthorizedRequestFilter() ;
    }

}
