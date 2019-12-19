package com.daiyanping.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @ClassName HystrixDashboardApp
 * @Description 使用HystrixDashboard对服务进行监控
 *
 *          进入到http://localhost:9090/hystrix/   页面
 *
 *          填写被监控的服务信息，如
 *
 *          http://localhost:8090/actuator/hystrix.stream
 *
 *          需要监控服务开启endpoint
 * @Author daiyanping
 * @Date 2019-09-12
 * @Version 0.1
 */
@EnableTurbine
@SpringBootApplication
public class TurbineApp {

    public static void main(String[] args) {
        SpringApplication.run(TurbineApp.class, args);
    }
}
