package com.daiyanping.springcloud.listener;

import com.daiyanping.springcloud.DefaultProcess;
import com.daiyanping.springcloud.Product;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
//@EnableBinding(Sink.class)
@EnableBinding(DefaultProcess.class)
public class MessageListener {
    @StreamListener(DefaultProcess.INPUT)
//    @StreamListener(Sink.INPUT)
    public void input(Message<Product> message) {
        System.err.println("【*** 消息接收 ***】" + message.getPayload());
    }
}