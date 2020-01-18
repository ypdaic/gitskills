package com.daiyanping.cms.rabbitmq.nativeapi.setmsg;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 *类说明：消息的属性的控制，request-response模式
 */
public class ReplyToProducer {

    public final static String EXCHANGE_NAME = "replyto";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.111.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("test1234");
        Connection connection = factory.newConnection();

        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建持久化交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",false);

        //响应QueueName ，消费者将会把要返回的信息发送到该Queue
        String responseQueue = channel.queueDeclare().getQueue();
        //消息的唯一id
        String msgId = UUID.randomUUID().toString();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .replyTo(responseQueue)
                .messageId(msgId)
                .build();

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(responseQueue,true,consumer);

        String msg = "Hellol,RabbitMq";
        channel.basicPublish(EXCHANGE_NAME,"error",
                properties,
                msg.getBytes());
        System.out.println("Sent error:"+msg);
    }

}
