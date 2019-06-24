package com.daiyanping.cms.dubbo.consumer.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.daiyanping.cms.dubbo.consumer.ConsumerService;
import com.daiyanping.cms.dubbo.provider.ProviderService;
//import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ConsumerServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-24
 * @Version 0.1
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {

//    @Autowired
    // 采用dubbo 注解的形式注入
    @Reference(interfaceClass = ProviderService.class, version = "1.0.0_annotation")
    ProviderService providerService;

    @Override
    public void say() {
        providerService.say();
    }
}
