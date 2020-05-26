package com.daiyanping.cms.kafka.nativeapi.seek;


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
import java.util.*;

/**
 * seek 从指定偏量进行消费
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

            // 指定分区从头消费，需要了解：一个分区的起始位置是 0 ，但并不代表每时每刻都为 0 ，因为日志清理的动作会清理旧的数据，所以分区的起始位置会自然而然地增加。
            // 这段代码可以用consumer.seekToBeginning(assignment); 替代
//            Map<TopicPartition, Long> beginOffsets = consumer.beginningOffsets(assignment);
//            for (TopicPartition tp : assignment) {
//                Long offset = beginOffsets.get(tp);
//                System.out.println("分区 " + tp + " 从 " + offset + " 开始消费");
//                consumer.seek(tp, offset);
//            }
//            consumer.seekToBeginning(assignment);


            // 指定分区从末尾消费，需要了解：endOffsets() 方法获取的是将要写入最新消息的位置，这段代码可以用consumer.seekToEnd(assignment);进行替代
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(assignment);
            for (TopicPartition tp : assignment) {
                Long offset = endOffsets.get(tp);
                System.out.println("分区 " + tp + " 从 " + offset + " 开始消费");
                consumer.seek(tp, offset);
            }


//            consumer.seekToEnd(assignment);

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
