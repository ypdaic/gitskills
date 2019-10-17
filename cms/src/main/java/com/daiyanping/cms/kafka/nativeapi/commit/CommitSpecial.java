package com.daiyanping.cms.kafka.nativeapi.commit;

import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import com.daiyanping.cms.kafka.nativeapi.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * 类说明：特定提交
 */
public class CommitSpecial {
    public static void main(String[] args) {
        /*消息消费者*/
        Properties properties = KafkaConst.consumerConfig(
                "CommitSpecial",
                StringDeserializer.class,
                StringDeserializer.class);
        /*取消自动提交*/
        properties.put("enable.auto.commit",false);

        /**
         * 提交的数据业务开控制
         */
        KafkaConsumer<String,String> consumer
                = new KafkaConsumer<String, String>(properties);
        Map<TopicPartition, OffsetAndMetadata> currOffsets
                = new HashMap<TopicPartition, OffsetAndMetadata>();
        int count = 0;
        try {
            consumer.subscribe(Collections.singletonList(
                    BusiConst.CONSUMER_COMMIT_TOPIC));
            while(true){
                ConsumerRecords<String, String> records
                        = consumer.poll(500);
                for(ConsumerRecord<String, String> record:records){
                    System.out.println(String.format(
                            "主题：%s，分区：%d，偏移量：%d，key：%s，value：%s",
                            record.topic(),record.partition(),record.offset(),
                            record.key(),record.value()));
//                    业务控制提交的主题，分区，偏移量
                    currOffsets.put(new TopicPartition(record.topic(),record.partition()),
                            new OffsetAndMetadata(record.offset()+1,"no meta"));
                    /**
                     * 每11条数据进行提交
                     */
                    if(count%11==0){
                        consumer.commitAsync(currOffsets,null);
                    }
                    count++;
                }
            }
        } finally {
            consumer.commitSync();
            consumer.close();
        }
    }
}
