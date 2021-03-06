package com.daiyanping.cms.rabbitmq.consumer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName RabbitTopicConsumer1
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-12
 * @Version 0.1
 */
@Component
@RabbitListener(queues = "topic.a")
public class RabbitTopicConsumer1 {

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    /**
     * 消息消费
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    @RabbitHandler
    public void recieved(String msg) {
        System.out.println(RabbitTopicConsumer1.class.getName() + "[string] recieved message:" + msg);
    }
}
