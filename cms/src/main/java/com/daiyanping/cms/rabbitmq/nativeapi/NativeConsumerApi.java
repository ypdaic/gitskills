package com.daiyanping.cms.rabbitmq.nativeapi;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NativeConsumerApi {

    public static void main(String[] args ) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.140.129");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin123");
//        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

//        声明交换器
        channel.exchangeDeclare("test", BuiltinExchangeType.DIRECT);
//        声明队列
        channel.queueDeclare("queue", false, false, false, null);
//        绑定队列，交换器，路由键
        channel.queueBind("queue", "test", "a");
        System.out.println("waiting for message ....");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String s = new String(body, "UTF-8");
                System.out.println("Received[:" + envelope.getRoutingKey() + "]");
            }
        };

        channel.basicConsume("queue", true, defaultConsumer);
        channel.close();
        connection.close();

    }
}
