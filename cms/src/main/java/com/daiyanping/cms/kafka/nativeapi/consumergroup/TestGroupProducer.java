package com.daiyanping.cms.kafka.nativeapi.consumergroup;

import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import com.daiyanping.cms.kafka.nativeapi.KafkaConst;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 *
 */
public class TestGroupProducer {

    private static KafkaProducer<String,String> producer = null;

    public static void main(String[] args) {
        /*发送配置的实例*/
        Properties properties
                = KafkaConst.producerConfig(StringSerializer.class,
                StringSerializer.class);
        /*消息生产者*/
        producer = new KafkaProducer<String, String>(properties);
        try {
            /*待发送的消息实例*/
            ProducerRecord<String,String> record;
            try {
                for(int i=0;i<50;i++){
                    record = new ProducerRecord<String,String>(
                            BusiConst.CONSUMER_GROUP_TOPIC,
                            "key"+i,"value"+i);
                    /*发送消息--发送后不管*/
                    producer.send(record);
                    System.out.println("数据["+record+"]已发送。");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            producer.close();
        }
    }




}
