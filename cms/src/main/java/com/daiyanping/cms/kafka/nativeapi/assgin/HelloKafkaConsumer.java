package com.daiyanping.cms.kafka.nativeapi.assgin;


import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * assign模式 手动指定消费的主题具体某个分区
 */
public class HelloKafkaConsumer {

    public static void main(String[] args) {
        LogManager.getRootLogger().setLevel(Level.ERROR);
        //TODO 消费者三个属性必须指定(broker地址清单、key和value的反序列化器)
        Properties properties = new Properties();
//        properties.put("bootstrap.servers","192.168.140.129:9092");
        properties.put("bootstrap.servers","127.0.0.1:9092");
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", StringDeserializer.class);

        //TODO 群组并非完全必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test1");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        try {
            //TODO assign模式指定消费某个分区
            TopicPartition topicPartition = new TopicPartition(BusiConst.HELLO_KAFKA2, 0);
            consumer.assign(Collections.singletonList(topicPartition));
            while(true){
                //TODO 拉取（新版本）
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for(ConsumerRecord<String, String> record:records){
                    System.out.println(String.format("topic:%s,分区：%d,偏移量：%d," + "key:%s,value:%s",record.topic(),record.partition(),
                            record.offset(),record.key(),record.value()));
                    //do my work
                    //打包任务投入线程池
                }
            }
        } finally {
            consumer.close();
        }

    }




}
