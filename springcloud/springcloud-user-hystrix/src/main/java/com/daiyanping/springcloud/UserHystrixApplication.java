package com.daiyanping.springcloud;

import com.daiyan.springcloud.WebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({WebSecurity.class})
@EnableEurekaClient
// 服务发现
@EnableDiscoveryClient
// 熔断支持
@EnableCircuitBreaker
public class UserHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserHystrixApplication.class, args);
    }

}
