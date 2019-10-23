package com.daiyanping.cms.rocketmq.nativeapi.normal;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 *
 * 同步发送
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sync");
        //producer.setRetryTimesWhenSendFailed(2);
        producer.setNamesrvAddr("192.168.140.129:9876");

        producer.start();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TopicTest" ,
                    "TagB" ,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            // 每个消息的发送必须等上个消费发送成功
            SendResult sendResult = producer.send(msg);
            // msgId 由rocket生成，queueId（一个主题，默认4个queue，相当于kafka的分区吧）
            System.out.printf("%s%n%n%n", sendResult.getSendStatus()+":(MsgId):"
                    +sendResult.getMsgId()+":(queueId):"
                    +sendResult.getMessageQueue().getQueueId()
                    +"(value):"+ new String(msg.getBody()));
        }
        producer.shutdown();
    }
}

