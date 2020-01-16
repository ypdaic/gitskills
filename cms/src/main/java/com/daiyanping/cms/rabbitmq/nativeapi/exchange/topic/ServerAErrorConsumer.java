package com.daiyanping.cms.rabbitmq.nativeapi.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
public class ServerAErrorConsumer {

    public static void main(String[] argv) throws IOException,
            InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.111.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("test1234");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(TopicProducer.EXCHANGE_NAME,
                BuiltinExchangeType.TOPIC);
        // 声明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName,TopicProducer.EXCHANGE_NAME,
                "error.*.A");

        System.out.println(" [*] Waiting for messages:");

        // 创建队列消费者
        final Consumer consumerA = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" AllConsumer Received "
                        + envelope.getRoutingKey()
                        + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumerA);
    }
}
