package com.daiyanping.cms.dubbo.provider;

import com.daiyanping.cms.entity.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProviderService {

    List<User> say();

    /**
     * 消费者异步调用
     * @param name
     * @return
     */
    CompletableFuture<String> sayHello(String name);

    /**
     * 服务提供者异步执行
     * @param name
     * @return
     */
    CompletableFuture<String> sayHello2(String name);
}
