package com.daiyanping.cms.kafka.nativeapi.selfserial;

import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import com.daiyanping.cms.kafka.nativeapi.DemoUser;
import com.daiyanping.cms.kafka.nativeapi.KafkaConst;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * 自定义序列器生成者
 */
public class SelfSerialProducer {

    private static KafkaProducer<String, DemoUser> producer = null;

    public static void main(String[] args) {

        /*消息生产者*/
        producer = new KafkaProducer<String, DemoUser>(KafkaConst.producerConfig(
                StringSerializer.class,SelfSerializer.class
        ));
        try {
            /*待发送的消息实例*/
            ProducerRecord<String,DemoUser> record;
            try {
                record =  new ProducerRecord<String,DemoUser>(
                        BusiConst.SELF_SERIAL_TOPIC,"user01",
                        new DemoUser(1,"mark"));
               producer.send(record);
               System.out.println("sent ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            producer.close();
        }
    }




}
