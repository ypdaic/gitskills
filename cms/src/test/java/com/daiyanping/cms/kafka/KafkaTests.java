package com.daiyanping.cms.kafka;

import com.daiyanping.cms.async.AsyncConfig;
import com.daiyanping.cms.kafka.springboot.KafkaConsumerConfig;
import com.daiyanping.cms.kafka.springboot.KafkaProducerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@RunWith,@SpringBootTest,@ContextConfiguration这三个注解，测试springboot项目需要用到
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration注解，加载我们指定的配置类
@ContextConfiguration(classes = {KafkaConsumerConfig.class, KafkaProducerConfig.class})
//@EnableAutoConfiguration注解用于开启自动配置，@EnableAsync注解开启的异步功能使用的线程池就是由
// 自动配置提供，由于我们不连数据库，就需要排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.daiyanping.cms.kafka.springboot")
public class KafkaTests {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Test
    public void test1() {
        kafkaTemplate.send("test", "test", "value");
    }

    @Test
    public void test2() {
        kafkaTemplate.send("testAck", "test", "value");
    }
}
