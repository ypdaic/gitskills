package com.daiyanping.cms.rabbitmq.spring.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 *类说明：
 */
@Component
public class AllLogTopicService implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(AllLogTopicService.class);
    public void onMessage(Message message) {
        logger.info("Get message: "+new String( message.getBody()));
    }
}
