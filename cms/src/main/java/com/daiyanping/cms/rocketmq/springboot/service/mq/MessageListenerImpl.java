package com.daiyanping.cms.rocketmq.springboot.service.mq;


import com.daiyanping.cms.rocketmq.springboot.model.OrderExp;
import com.daiyanping.cms.rocketmq.springboot.service.busi.DlyOrderProcessor;
import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author King老师
 *
 *类说明：处理消息队列返回的延时订单
 */
@Service
public class MessageListenerImpl implements MessageListenerConcurrently {
    private Logger logger = LoggerFactory.getLogger(MessageListenerImpl.class);
    @Autowired
    private DlyOrderProcessor processDlyOrder;

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            try {
                //TODO 使用GSON反序列化
                String txtMsg = new String(msg.getBody());
                Gson gson = new Gson();
                System.out.println("接收到RocketMQ的消息："+txtMsg);
                OrderExp order = (OrderExp)gson.fromJson(txtMsg, OrderExp.class);

                //TODO 修改订单状态为过期
                if(order.getId()!=null){
                    processDlyOrder.checkDelayOrder(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        // 如果没有异常会认为都成功消费
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
