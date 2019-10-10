package com.daiyanping.springcloud.controller;

import com.daiyanping.springcloud.config.InfoConfig;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${spring.profiles.active}")
    private List<String> profile;

    @Autowired
    private InfoConfig infoConfig;

    @GetMapping("/config")
    public String getConfig() {
        return "ApplicationName = " + this.applicationName + "、EurekaServers = "
                + this.eurekaServers + " 、profiles=" + profile + " 、" + infoConfig.getAppName();
    }
}
