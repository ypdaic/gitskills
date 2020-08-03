package com.daiyanping.cms.rabbitmq.nativeapi.exchange.direct;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * direct 交换器
 */
public class DirectProducer {

    public final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("192.168.111.128");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("test1234");
        Connection connection = null;
        Channel channel = null;
        try {
            // 获取连接
            connection = connectionFactory.newConnection();
            // 获取信道
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            String[] serverRoutKey = {"error", "info", "warn"};

//            for (int i = 0; i < serverRoutKey.length; i++) {
//                String serverMessage = serverRoutKey[i];
//                String message = new String("Hello Rabbit" + serverMessage);
//                // 发送消息，第一个参数指定交换器，第二个参数指定路由键，第4个参数指定我们要发生的消息
//                channel.basicPublish(EXCHANGE_NAME, serverMessage, null, message.getBytes());
//                System.out.println("Send " + serverMessage + ":" + message);
//
//            }
            for (int i = 0; i < 2000; i++) {
//                String serverMessage = serverRoutKey[i];
                String message = new String("test" + i);
                // 发送消息，第一个参数指定交换器，第二个参数指定路由键，第4个参数指定我们要发生的消息

                AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                builder.deliveryMode(2);
                AMQP.BasicProperties properties = builder.build();


                channel.basicPublish(EXCHANGE_NAME, "", properties, message.getBytes());
                System.out.println("Send :" + message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
