package com.daiyanping.springcloud;

import com.daiyan.springcloud.WebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Import;

/**
 * @ClassName ConfigApp
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-24
 * @Version 0.1
 */
@SpringBootApplication
@EnableConfigServer
@Import({WebSecurity.class})
public class ConfigApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApp.class, args);
    }

}
