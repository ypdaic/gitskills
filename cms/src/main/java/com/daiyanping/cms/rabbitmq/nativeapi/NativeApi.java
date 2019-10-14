package com.daiyanping.cms.rabbitmq.nativeapi;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NativeApi {

    public static void main(String[] args ) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.140.129:5672");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin123");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

//        声明交换器
        channel.exchangeDeclare("test", BuiltinExchangeType.DIRECT);

//        路由键
        String[] routeKeys = {"a", "b", "c"};
        for (int i = 0; i < 3; i++) {
            String routeKey = routeKeys[i];
            String msg = "Hello, Rabbitmq" + i;
//            发布消息
            channel.basicPublish("test", routeKey, null, msg.getBytes());
            System.out.println("Sent:" + routeKey + msg);
        }

        channel.close();
        connection.close();


    }

}
