package com.daiyanping.cms.kafka.nativeapi.seek;


import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 原生api
 */
public class HelloKafkaProducer {

    public static void main(String[] args) {
        //TODO 生产者三个属性必须指定(broker地址清单、key和value的序列化器)
        Properties properties = new Properties();
        // 清单里不需要包含所有的broker地址，生产者会从给定的broker里查询其他broker的信息。不过最少提供2个broker的信息，一旦其中一个宕机，生产者仍能连接到集群上。
//        properties.put("bootstrap.servers","192.168.140.129:9092");
        properties.put("bootstrap.servers","127.0.0.1:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        try {
            ProducerRecord<String,String> record;
            try {
                //TODO 发送4条消息
                for(int i=0;i< 20;i++){
                    /**
                     * 指定key,会根据一定规则将消息放到指定分区,key为null的话就是所有分区均分消息
                     */
                    record = new ProducerRecord<String,String>(BusiConst.HELLO_KAFKA2, "7" + i,"lison" + i);
                    //发送并忘记,发送失败会重试，可能会丢消息
                    producer.send(record);
                    System.out.println(i+"，message is sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            producer.close();
        }
    }


}
