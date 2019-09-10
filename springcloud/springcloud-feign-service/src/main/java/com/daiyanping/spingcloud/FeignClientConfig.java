package com.daiyanping.spingcloud;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FeignClientConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-10
 * @Version 0.1
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public BasicAuthRequestInterceptor getBasicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "test1234");
    }
}
