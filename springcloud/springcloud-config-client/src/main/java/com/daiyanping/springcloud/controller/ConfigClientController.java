package com.daiyanping.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ConfigClientController
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-25
 * @Version 0.1
 */
@RestController
public class ConfigClientController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServers;

    /**
     * 基于springcloud-config需要这取值
     */
    @Value("${spring.profiles.active[0]}")
    private String profile;

    @GetMapping("/config")
    public String getConfig() {
        return "ApplicationName = " + this.applicationName + "、EurekaServers = "
                + this.eurekaServers;

//        return "ApplicationName = " + this.applicationName + " 、profiles=" + profile;
    }
}
