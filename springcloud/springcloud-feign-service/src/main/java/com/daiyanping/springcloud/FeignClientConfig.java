package com.daiyanping.springcloud;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FeignClientConfig
 * @Description TODO 千万别将该放置在主应用程序上下文@ComponentScan 所扫描的包中
 * TODO 否则，该配置将会被所有Feign Client共享（相当于变成了通用配置，其实本质还是Spring父子上下文扫描包重叠导致的问题），
 * TODO 无法实现细粒度配置！
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

//    开启feign调用日志
    @Bean
    public Logger.Level getFeignLoggerLevel() {
        return feign.Logger.Level.FULL ;
    }
}
