package com.daiyanping.cms.rabbitmq.nativeapi.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：异步确认，什么时候确认由rabbitmq自己决定
 */
public class ProducerConfirmAsync {

    public final static String EXCHANGE_NAME = "producer_async_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * 创建连接连接到MabbitMQ
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

        // 启用发送者确认模式
        channel.confirmSelect();

        // 异步确认
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple)
                    throws IOException {
                System.out.println("deliveryTag:"+deliveryTag
                        +",multiple:"+multiple);

            }

            public void handleNack(long deliveryTag, boolean multiple)
                    throws IOException {

            }
        });

        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq路由失败:  "+routingKey+"."+message);
            }
        });

        String[] severities={"error","warning"};
        for(int i=0;i<100;i++){
            String severity = severities[i%2];
            // 发送的消息
            String message = "Hello World_"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME, severity, true,
                    MessageProperties.PERSISTENT_BASIC, message.getBytes());
            System.out.println("----------------------------------------------------");
            System.out.println(" Sent Message: [" + severity +"]:'"+ message + "'");
        }

        // 关闭频道和连接
        //channel.close();
        //connection.close();
    }


}
