package com.daiyanping.cms.rocketmq.nativeapi.ordermessage.all;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 *
 * 同步发送--顺序发送
 */
public class ProducerAllOrder {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("AllOrder");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("AllOrder" ,
                    "TagA" ,"KEY" + i,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            SendResult sendResult = producer.send(msg, 100000);
            System.out.printf("%s%n", sendResult.getSendStatus()+":"+ new String(msg.getBody()));
        }
        producer.shutdown();
    }
}

