package com.daiyanping.cms.dubbo.provider.impl;


import com.daiyanping.cms.dubbo.provider.ProviderService;
import com.daiyanping.cms.entity.User;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
@Service(version = "1.0.0_annotation")
public class ProviderServiceImpl implements ProviderService {

    @Override
    public List<User> say() {
        String key = RpcContext.getContext().getAttachment("key");
        System.out.println(key);
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setName("demo");
        user.setAge(28);
        users.add(user);
        return users;
    }

    /**
     * 返回CompletableFuture，客户端不用开启多线程，即可并行调用多个服务
     * @param name
     * @return
     */
    @Override
    public CompletableFuture<String> sayHello(String name) {
        CompletableFuture<String> stringCompletableFuture = new CompletableFuture<>();
        stringCompletableFuture.complete(name);
        return stringCompletableFuture;
    }

    @Override
    public CompletableFuture<String> sayHello2(String name) {
        return CompletableFuture.supplyAsync(() -> {
            return name;
        });
    }


}
