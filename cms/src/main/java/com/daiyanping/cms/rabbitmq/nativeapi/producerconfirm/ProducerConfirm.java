package com.daiyanping.cms.rabbitmq.nativeapi.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：消费者确认：一条一条确认
 *
 *  当我们只开启生成者不开启消费者会发生什么情况呢
 *
 *  Sent Message: [error]:'Hello World_1'
 * RabbitMq返回的replyCode:  312
 * RabbitMq返回的replyText:  NO_ROUTE
 * RabbitMq返回的exchange:  producer_confirm
 * RabbitMq返回的routingKey:  error
 * RabbitMq返回的message:  Hello World_1
 * 发送者确认成功
 *  Sent Message: [error]:'Hello World_2'
 * RabbitMq返回的replyCode:  312
 * RabbitMq返回的replyText:  NO_ROUTE
 * RabbitMq返回的exchange:  producer_confirm
 * RabbitMq返回的routingKey:  error
 * RabbitMq返回的message:  Hello World_2
 * 发送者确认成功
 *
 * 从上面的打印可以看到，虽然没有消费者，但是消费者确认是响应ok的
 */
public class ProducerConfirm {

    public final static String EXCHANGE_NAME = "producer_confirm";
    private final static String ROUTE_KEY = "error";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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
        channel.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println("channel关闭监听，关闭原因：" + cause);
            }
        });
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {

                String message = new String(body);
                System.out.println("RabbitMq返回的replyCode:  "+replyCode);
                System.out.println("RabbitMq返回的replyText:  "+replyText);
                System.out.println("RabbitMq返回的exchange:  "+exchange);
                System.out.println("RabbitMq返回的routingKey:  "+routingKey);
                System.out.println("RabbitMq返回的message:  "+message);
            }
        });
        // 启用发送者确认模式
        channel.confirmSelect();

        //所有日志严重性级别
        for(int i=0;i<2;i++){
            // 发送的消息
            String message = "Hello World_"+(i+1);
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish("sfdsfsf", ROUTE_KEY, true,null, message.getBytes());
            System.out.println(" Sent Message: [" + ROUTE_KEY +"]:'"+ message + "'");
            // 一条一条确认
            if(channel.waitForConfirms()){
                System.out.println("发送者确认成功");
            }else{
                System.out.println("发送者确认失败");
            }
        }
        // 关闭频道和连接
//        channel.close();
//        connection.close();
    }

}
