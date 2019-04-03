package com.daiyanping.cms;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ConfigurationPropertiesTest2
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@ConfigurationProperties(prefix = "configuration-properties-test")
public class ConfigurationPropertiesTest2 {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
