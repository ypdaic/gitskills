package com.daiyanping.cms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FactoryBeanConfig
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-30
 * @Version 0.1
 */
@Configuration
public class FactoryBeanConfig {

    @Bean
    public FactoryBeanTest getFactoryBeanTest() {
        return new FactoryBeanTest();
    }

}
