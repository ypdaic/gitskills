package com.daiyanping.cms.rabbitmq.nativeapi.getmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：拉取模式
 */
public class GetMessageConsumer {


    public static void main(String[] argv)
            throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.111.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("test1234");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(GetMessageProducer.EXCHANGE_NAME,
                "direct");
        // 声明一个队列
        String queueName = "focuserror";
        channel.queueDeclare(queueName,
                false,false,
                false,null);

        String severity="error";//只关注error级别的日志，然后记录到文件中去。
        channel.queueBind(queueName,
                GetMessageProducer.EXCHANGE_NAME, severity);

        System.out.println(" [*] Waiting for messages......");

        while(true){
            // 拉取自动应答
            GetResponse getResponse = channel.basicGet(queueName, true);
            if(null!=getResponse){
                System.out.println("received["
                        +getResponse.getEnvelope().getRoutingKey()+"]"
                        +new String(getResponse.getBody()));
            }
            Thread.sleep(1000);
        }


    }

}
