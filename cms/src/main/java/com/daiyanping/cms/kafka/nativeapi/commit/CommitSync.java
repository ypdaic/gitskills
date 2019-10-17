package com.daiyanping.cms.kafka.nativeapi.commit;

import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import com.daiyanping.cms.kafka.nativeapi.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 *
 * 类说明：同步的方式手动提交当偏移量，生产者使用ProducerCommit
 * ./kafka-topics.sh --zookeeper localhost:2181 --create --topic simple  --replication-factor 1 --partitions 2
 * 创建两个分区
 */
public class CommitSync {

    /**
     * 消费结果，可以看到虽然有两个分区，但是每个消费者只能消费一个分区
     * 主题：simple，分区：0，偏移量：0，key：key0，value：value0
     * 主题：simple，分区：0，偏移量：1，key：key1，value：value1
     * 主题：simple，分区：0，偏移量：2，key：key6，value：value6
     * 主题：simple，分区：0，偏移量：3，key：key10，value：value10
     * 主题：simple，分区：0，偏移量：4，key：key11，value：value11
     * 主题：simple，分区：0，偏移量：5，key：key14，value：value14
     * 主题：simple，分区：0，偏移量：6，key：key15，value：value15
     * 主题：simple，分区：0，偏移量：7，key：key17，value：value17
     * 主题：simple，分区：0，偏移量：8，key：key18，value：value18
     * 主题：simple，分区：0，偏移量：9，key：key19，value：value19
     * 主题：simple，分区：0，偏移量：10，key：key20，value：value20
     * 主题：simple，分区：0，偏移量：11，key：key22，value：value22
     * 主题：simple，分区：0，偏移量：12，key：key23，value：value23
     * 主题：simple，分区：0，偏移量：13，key：key24，value：value24
     * 主题：simple，分区：0，偏移量：14，key：key25，value：value25
     * 主题：simple，分区：0，偏移量：15，key：key27，value：value27
     * 主题：simple，分区：0，偏移量：16，key：key29，value：value29
     * 主题：simple，分区：0，偏移量：17，key：key34，value：value34
     * 主题：simple，分区：0，偏移量：18，key：key35，value：value35
     * 主题：simple，分区：0，偏移量：19，key：key36，value：value36
     * 主题：simple，分区：0，偏移量：20，key：key40，value：value40
     * 主题：simple，分区：0，偏移量：21，key：key41，value：value41
     * 主题：simple，分区：0，偏移量：22，key：key43，value：value43
     * 主题：simple，分区：0，偏移量：23，key：key45，value：value45
     * 主题：simple，分区：0，偏移量：24，key：key46，value：value46
     * 主题：simple，分区：0，偏移量：25，key：key47，value：value47
     * 主题：simple，分区：0，偏移量：26，key：key49，value：value49
     * @param args
     */
    public static void main(String[] args) {
        /*消息消费者*/
        Properties properties = KafkaConst.consumerConfig("CommitSync",
                StringDeserializer.class,
                StringDeserializer.class);
        /*取消自动提交*/
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);

        KafkaConsumer<String,String> consumer
                = new KafkaConsumer<String, String>(properties);
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
                    //do our work

                }
                //开始事务
                //读业务写数据库-
                //偏移量写入数据库
                //提交，同步方式，全部提交ok才能往下走
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }
}
