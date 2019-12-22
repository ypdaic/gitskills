package com.daiyanping.springcloud.service.impl;


import com.daiyanping.springcloud.DefaultProcess;
import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

//@EnableBinding(Source.class)

// 自定义的通道
@EnableBinding(DefaultProcess.class)
public class MessageProviderImpl implements IMessageProvider {

    @Resource
    // 引入自定义的通道
    @Qualifier("enjoy_output")
    private MessageChannel output;  // 消息的发送管道

    @Override
    public void send(Product product) {
        output.send(MessageBuilder.withPayload(product).build());
    }
}