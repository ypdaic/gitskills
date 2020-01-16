package com.daiyanping.cms.rabbitmq.nativeapi.transaction;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：事务支持
 */
public class ProducerTransaction {

    public final static String EXCHANGE_NAME = "producer_transaction";

    public static void main(String[] args)
            throws IOException, TimeoutException, InterruptedException {
        /**
         * 创建连接连接到RabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("192.168.111.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("test1234");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String[] severities={"error","info","warning"};
        // 开启事务
        channel.txSelect();
        try {
            for(int i=0;i<3;i++){
                String severity = severities[i%3];
                // 发送的消息
                String message = "Hello World_"+(i+1)
                        +("_"+System.currentTimeMillis());
                channel.basicPublish(EXCHANGE_NAME, severity, true,
                        null, message.getBytes());
                System.out.println("----------------------------------");
                System.out.println(" Sent Message: [" + severity +"]:'"
                        + message + "'");
                Thread.sleep(200);
            }
            // 提交事务
            channel.txCommit();
        } catch (IOException e) {
            e.printStackTrace();
//            回滚事务
            channel.txRollback();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 关闭频道和连接
        channel.close();
        connection.close();
    }


}
