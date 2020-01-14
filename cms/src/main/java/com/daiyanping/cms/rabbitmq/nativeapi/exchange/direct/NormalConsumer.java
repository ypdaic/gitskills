package com.daiyanping.cms.rabbitmq.nativeapi.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NormalConsumer {

    public final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.111.128");
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
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 声明一个队列
            String queueName = "focuserror";
            // 声明一个队列
            channel.queueDeclare(queueName, false, false, false, null);
            String routKey = "error";

            // 将队列绑定到交换器上，并指定路由key
            channel.queueBind(queueName, EXCHANGE_NAME, routKey);

            System.out.println("waiting for message....");

            // 声明一个消费者
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                // 收到消息处理
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
                }
            };

            // 消费者正式开始消费
            channel.basicConsume(queueName, defaultConsumer);
        }
      catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
