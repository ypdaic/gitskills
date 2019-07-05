package com.daiyanping.cms.dubbo.consumer.impl;

import com.daiyanping.cms.dubbo.consumer.ConsumerService;
import com.daiyanping.cms.dubbo.provider.ProviderService;
//import jdk.nashorn.internal.ir.annotations.Reference;
import com.daiyanping.cms.entity.User;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        // 隐私参数传递
        RpcContext.getContext().setAttachment("key", "test");
        List<User> say = providerService.say();
        System.out.println(say);
        boolean isConsumerSide = RpcContext.getContext().isConsumerSide();
        // 获取最后一次调用的提供方IP地址
        String serverIP = RpcContext.getContext().getRemoteHost();
        System.out.println(serverIP);
        // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
        String application = RpcContext.getContext().getUrl().getParameter("application");
        System.out.println(application);

        CompletableFuture<String> future = providerService.sayHello("异步调用");
        future.whenComplete((value, exception) -> {
            System.out.println(value);
        });
        System.out.println("早于结果输出");

        CompletableFuture<String> future1 = providerService.sayHello2("服务端异步执行");
        future1.thenAccept((value) -> {
            System.out.println(value);
        });
//        future1.whenComplete((value, exception) -> {
//            System.out.println(value);
//        });
        System.out.println("早于结果输出");

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {

        }
    }

}
