package com.daiyanping.cms.rabbitmq.nativeapi.msgdurable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：消息的持久化生产者
 */
public class MsgAttrProducer {

    public final static String EXCHANGE_NAME = "msg_durable";

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
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);

        /*日志消息级别，作为路由键使用*/
        String[] serverities = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String severity = serverities[i%3];
            String msg = "Hellol,RabbitMq"+(i+1);
            /*发布持久化消息*/
            channel.basicPublish(EXCHANGE_NAME,severity,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    msg.getBytes());
        }

        channel.close();
        connection.close();

    }

}
