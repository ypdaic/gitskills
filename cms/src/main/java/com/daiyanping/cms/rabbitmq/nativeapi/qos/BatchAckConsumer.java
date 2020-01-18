package com.daiyanping.cms.rabbitmq.nativeapi.qos;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 类说明：批量确认消费者
 */
public class BatchAckConsumer extends DefaultConsumer {

    private int messasgeCount = 0;

    public BatchAckConsumer(Channel channel) {
        super(channel);
        System.out.println("批量消费者启动...");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println("批量消费者Received["+envelope.getRoutingKey()
                +"]"+message);
        messasgeCount++;
        if (messasgeCount % 50==0){
//            手动进行批量确认
            this.getChannel().basicAck(envelope.getDeliveryTag(),
                    true);
            System.out.println("批量消费者进行消息的确认-------------");
        }
        if(message.equals("stop")){
            this.getChannel().basicAck(envelope.getDeliveryTag(),
                    true);
            System.out.println("批量消费者进行最后部分业务消息的确认-------------");
        }
    }
}
