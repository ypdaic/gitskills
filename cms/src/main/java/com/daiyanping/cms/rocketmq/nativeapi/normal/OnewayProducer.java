package com.daiyanping.cms.rocketmq.nativeapi.normal;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 *单向发送（同kafka的发送并放弃）
 */
public class OnewayProducer {
    public static void main(String[] args) throws Exception{
        //生产者实例化，和kafka不一样，rocket生成者也提供群组（存在群组的情况下，只允许一个实例发送消息）
        DefaultMQProducer producer = new DefaultMQProducer("oneway");
        //指定rocket服务器地址（nameServer地址）
        //producer.setNamesrvAddr("localhost:9876");
        producer.setNamesrvAddr("192.168.140.129:9876");

        //启动实例
        producer.start();
        for (int i = 0; i < 10; i++) {
            //创建一个消息实例，指定topic、tag和消息体
            // 在没有创建主题的前提下，下面的消息依然可以创建，是开启了主题自动创建配置项
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //发送消息
            producer.sendOneway(msg);
            System.out.printf("%s%n",  new String(msg.getBody()));
        }
        //生产者实例不再使用时关闭.
        producer.shutdown();
    }
}
