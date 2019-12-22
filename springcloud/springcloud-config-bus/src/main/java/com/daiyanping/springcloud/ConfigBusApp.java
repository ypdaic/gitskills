package com.daiyanping.springcloud;

import com.daiyan.springcloud.WebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Import;

/**
 * @ClassName ConfigApp
 * @Description TODO 基于消息总线的config-server
 * 有配置更新后需要发送post请求触发更新，如下：
 * http://localhost:9005/actuator/bus-refresh
 * @Author daiyanping
 * @Date 2019-09-24
 * @Version 0.1
 */
@SpringBootApplication
@EnableConfigServer
@Import({WebSecurity.class})
public class ConfigBusApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigBusApp.class, args);
    }

}
