package com.daiyanping.cms.rocketmq.springboot.service.mq;


import com.daiyanping.cms.rocketmq.springboot.model.OrderExp;
import com.daiyanping.cms.rocketmq.springboot.service.busi.DlyOrderProcessor;
import com.daiyanping.cms.rocketmq.springboot.service.delay.IDelayOrder;
import com.google.gson.Gson;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 *@author King老师
 *
 *类说明：消息队列的实现
 */
@Service
@Qualifier("rocketmq")
public class RocketMQProducer implements IDelayOrder {

    @Autowired
    private DlyOrderProcessor processDelayOrder;
    private Thread takeOrder;

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducer.class);

    private DefaultMQProducer defaultMQProducer;
    private String producerGroup;
    private String namesrvAddr;

    @PostConstruct
    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(this.producerGroup);
        defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        defaultMQProducer.start();
        logger.info("rocketMQ初始化生产者完成[producerGroup：" + producerGroup + "]");
    }
    @PreDestroy
    public void destroy() {
        defaultMQProducer.shutdown();
        logger.info("rocketMQ生产者[producerGroup: " + producerGroup + "]已停止");
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }


    public void orderDelay(OrderExp order, long timeLevel) {
        try {
            //TODO 使用Gson序列化
            Gson gson = new Gson();
            String txtMsg = gson.toJson(order);
            //TODO 发送延时消息
            Message msg = new Message("TimeOrder", null, txtMsg.getBytes());
            //这个是设置延时消息的属性
            //"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"  18个等级
            msg.setDelayTimeLevel((int)timeLevel);
            SendResult result = defaultMQProducer.send(msg);

            if(result.getSendStatus() !=null && result.getSendStatus()== SendStatus.SEND_OK){
                System.out.println("订单被推入延迟队列，订单详情:"+order);
                logger.info("订单被推入延迟队列，订单详情："+order);
            }else{
                logger.error("订单推入RocketMq失败，订单详情："+order+"SendStatus:"+result.getSendStatus());
            }

        } catch (Exception e) {
            logger.error("单推入RocketMq失败，失败详情："+e.toString());
        }
    }
}
