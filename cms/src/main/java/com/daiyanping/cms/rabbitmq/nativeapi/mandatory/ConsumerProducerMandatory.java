package com.daiyanping.cms.rabbitmq.nativeapi.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：
 */
public class ConsumerProducerMandatory {

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.111.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("test1234");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(ProducerMandatory.EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();

        //只关注error级别的日志
        String severity="error";
        channel.queueBind(queueName, ProducerMandatory.EXCHANGE_NAME,
                severity);

        System.out.println(" [*] Waiting for messages......");

        // 创建队列消费者
        final Consumer consumerB = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                //记录日志到文件：
                System.out.println( "Received ["+ envelope.getRoutingKey()
                        + "] "+message);
            }
        };
        channel.basicConsume(queueName, true, consumerB);
    }

}
