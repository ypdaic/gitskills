package com.daiyanping.cms.spring.bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan(basePackages = {"com.daiyanping.cms.spring.dao", "com.daiyanping.cms.spring.service", "com.daiyanping.cms.spring.datasource", "com.daiyanping.cms.spring.transaction", "com.daiyanping.cms.spring.bean", "com.daiyanping.cms.spring.aop"})
public class ComponentScanBean {

//    @Bean
//    public void xx() {
//        System.out.println("==");
//    }
}
