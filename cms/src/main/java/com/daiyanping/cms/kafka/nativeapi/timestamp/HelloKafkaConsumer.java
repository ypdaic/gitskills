package com.daiyanping.cms.kafka.nativeapi.timestamp;


import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.time.Duration;
import java.util.*;

/**
 * offsetsForTimes 从指定时间戳的位置进行消费
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
            //TODO seek 从指定偏移量处进行消费，assign和subscribe 都是支持seek的
            TopicPartition topicPartition = new TopicPartition(BusiConst.HELLO_KAFKA2, 0);
//            consumer.subscribe(Collections.singletonList(BusiConst.HELLO_KAFKA2));
            consumer.assign(Collections.singletonList(topicPartition));

            // 从指定偏移量处进行消费，但是直接seek是不可以的，seek() 方法只能重置消费者分配到的分区的消费位置，而分区的分配是在 poll() 方法的调用过程中实现的，也就是说，在执行 seek() 方法之前需要先执行一次 poll() 方法，等到分配到分区之后才可以重置消费位置。
//            consumer.seek(topicPartition, 1);

            Set<TopicPartition> assignment = new HashSet<>();
            // 在poll()方法内部执行分区分配逻辑，该循环确保分区已被分配。
            // 当分区消息为0时进入此循环，如果不为0，则说明已经成功分配到了分区。
            while (assignment.size() == 0) {
                consumer.poll(100);
                // assignment()方法是用来获取消费者所分配到的分区消息的
                // assignment的值为：topic-demo-3, topic-demo-0, topic-demo-2, topic-demo-1
                assignment = consumer.assignment();
            }

            Map<TopicPartition, Long> timestampToSearch = new HashMap<>();
            for (TopicPartition tp : assignment) {
                // 设置查询分区时间戳的条件：获取当前时间前一天之后的消息
                timestampToSearch.put(tp, System.currentTimeMillis() - 24 * 3600 * 1000);
            }

            // 获取时间戳位置的偏移量
            // timestampToSearch的值为{topic-demo-0=1563709541899, topic-demo-2=1563709541899, topic-demo-1=1563709541899}
            Map<TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(timestampToSearch);

            for(TopicPartition tp: assignment){
                // 获取该分区的offset以及timestamp
                OffsetAndTimestamp offsetAndTimestamp = offsets.get(tp);
                // 如果offsetAndTimestamp不为null，则证明当前分区有符合时间戳条件的消息
                if (offsetAndTimestamp != null) {
                    consumer.seek(tp, offsetAndTimestamp.offset());
                }
            }

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
