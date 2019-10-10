package com.daiyanping.springcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @ClassName InfoConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-10
 * @Version 0.1
 */
@Component
@RefreshScope
public class InfoConfig {

    @Value("${info.app.name}")
    private String appName ;
    @Value("${info.company.name}")
    private String companyName ;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
