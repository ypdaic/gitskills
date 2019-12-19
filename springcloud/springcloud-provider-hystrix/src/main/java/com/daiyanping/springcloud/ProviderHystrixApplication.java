package com.daiyanping.springcloud;

import com.daiyan.springcloud.WebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

/**
 * http://localhost:8088/actuator/hystrix.stream
 * hystrix监控信息，需要将整个连接加到hystrix-dashboard中进行监控
 * 如果有认证需要加认证
 * http://admin:test1234@localhost:8088/actuator/hystrix.stream
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({WebSecurity.class})
@EnableEurekaClient
// 服务发现
@EnableDiscoveryClient
// 熔断支持
@EnableCircuitBreaker
public class ProviderHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderHystrixApplication.class, args);
    }

}
