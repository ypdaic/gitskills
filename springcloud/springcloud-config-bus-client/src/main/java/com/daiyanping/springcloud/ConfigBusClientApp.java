package com.daiyanping.springcloud;

import com.daiyan.springcloud.WebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @ClassName ConfigBusClientApp
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-25
 * @Version 0.1
 */
@SpringBootApplication
@Import({WebSecurity.class})
public class ConfigBusClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigBusClientApp.class, args);
    }
}
