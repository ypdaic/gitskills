package com.daiyanping.cms.kafka.nativeapi.producerConfig;

import com.daiyanping.cms.kafka.nativeapi.BusiConst;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 *o
 */
public class ConfigKafkaProducer {

    public static void main(String[] args) {
        //TODO 生产者三个属性必须指定(broker地址清单、key和value的序列化器)
        Properties properties = new Properties();
        properties.put("bootstrap.servers","192.168.140.129:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        /**
         * ----------------TODO 更多发送配置（重要的）----------------------------
         */

        /**
         * ack 0,1,all 0表示发送就不管了，1表示发送到首领就表示发送成功，all表示发送到首领和副本都成功才表示发送成功
         * 如果有多个副本，配置all,性能会比较差
         */
        properties.put("acks","1");
        /**
         * 一个批次可以使用的内存大小 缺省16384(16k)
         */
        properties.put("batch.size",16384);
        /**
         * 指定了生产者在发送批次前等待更多消息加入批次的时间,  缺省0
         * 0就表示不等待，有消息就发送了（也就是一条条走），如果想要性能高，可以配置等50ms
         * 然后一起发送，配合batch.size配置一起使用
         */
        properties.put("linger.ms",0L);
        /**
         * 控制生产者发送请求最大大小,默认1M （这个参数和Kafka主机的message.max.bytes 参数有关系）
         * 如果比服务器的大就会发送数据丢失
         */
        properties.put("max.request.size",1 * 1024 * 1024);

        /**
         * -----------------TODO 更多发送配置（非重要的）-------------------------
         */
        /**
         * 生产者内存缓冲区大小
         */
        properties.put("buffer.memory",32 * 1024 * 1024L);
        /**
         * 重发消息次数,默认int max，在多分区下顺序保证不好做，存在重试机制，导致第一条消息失败重试，排在了第二天消息的后面
         * 但是不重试有存在消息丢失的风险
         */
        properties.put("retries",0);
        properties.put("request.timeout.ms",30 * 1000);//客户端将等待请求的响应的最大时间 默认30秒
        properties.put("max.block.ms",60*1000);//最大阻塞时间，超过则抛出异常 缺省60000ms

        properties.put("compression.type","none"); // 于压缩数据的压缩类型。默认是无压缩 ,none、gzip、snappy


        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        try {
            ProducerRecord<String,String> record;
            try {
                //TODO发送4条消息
                for(int i=0;i<4;i++){
                    record = new ProducerRecord<String,String>(BusiConst.HELLO_TOPIC, String.valueOf(i),"lison");
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
