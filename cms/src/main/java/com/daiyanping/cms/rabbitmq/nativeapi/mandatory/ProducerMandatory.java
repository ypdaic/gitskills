package com.daiyanping.cms.rabbitmq.nativeapi.mandatory;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：失败通知
 */
public class ProducerMandatory {

    public final static String EXCHANGE_NAME = "mandatory_test";

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

        //连接关闭时执行
        connection.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println(cause.getMessage());
            }
        });

        //信道关闭时执行
        channel.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println(cause.getMessage());
            }
        });

        // 添加失败通知
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("返回的replyText ："+replyText);
                System.out.println("返回的exchange ："+exchange);
                System.out.println("返回的routingKey ："+routingKey);
                System.out.println("返回的message ："+message);
            }
        });

        String[] severities={"error","info","warning"};
        for(int i=0;i<3;i++){
            String severity = severities[i%3];
            // 发送的消息
            String message = "Hello World_"+(i+1)
                    +("_"+System.currentTimeMillis());
//            第3个参数设置为true,开启失败通知
            channel.basicPublish(EXCHANGE_NAME,severity,true,
                    null,message.getBytes());
            System.out.println("----------------------------------");
            System.out.println(" Sent Message: [" + severity +"]:'"
                    + message + "'");
            Thread.sleep(200);
        }

        // 关闭频道和连接
        channel.close();
        connection.close();
    }


}
