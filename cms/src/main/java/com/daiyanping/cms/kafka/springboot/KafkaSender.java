package com.daiyanping.cms.kafka.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 *
 * 类说明：
 */
@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void messageSender(String tpoic,String key,String message){
        try {
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(tpoic, key, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
