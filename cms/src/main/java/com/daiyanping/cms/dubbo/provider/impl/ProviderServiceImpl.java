package com.daiyanping.cms.dubbo.provider.impl;

import com.alibaba.dubbo.config.annotation.Method;
import com.alibaba.dubbo.config.annotation.Service;
import com.daiyanping.cms.dubbo.provider.ProviderService;
//import org.springframework.stereotype.Service;

/**
 * @ClassName ProviderServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-24
 * @Version 0.1
 */
//@Service
// 使用dubbo注解形式
@Service(version = "1.0.0_annotation", methods = {@Method(name = "say", timeout = 250, retries = 0)})
public class ProviderServiceImpl implements ProviderService {

    @Override
    public void say() {
        System.out.println("hello word");
    }
}
