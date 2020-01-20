package com.daiyanping.cms.rabbit;

import com.daiyanping.cms.rabbitmq.RabbitmqConfig;
import com.daiyanping.cms.rabbitmq.producer.RabbitProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith,@SpringBootTest,@ContextConfiguration这三个注解，测试springboot项目需要用到
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration注解，加载我们指定的配置类
@ContextConfiguration(classes = {RabbitmqConfig.class})
//@EnableAutoConfiguration注解用于开启自动配置，@EnableAsync注解开启的异步功能使用的线程池就是由
// 自动配置提供，由于我们不连数据库，就需要排除springjdbc自动配置
@EnableAutoConfiguration(exclude = {RedissonAutoConfiguration.class, DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class, SpringDataWebAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class RabbitTests {

    @Autowired
    private RabbitProducer rabbitProducer;

    @Test
    public void test() {
        rabbitProducer.stringSend();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        rabbitProducer.fanoutSend();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        rabbitProducer.topicTopic1Send();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        rabbitProducer.topicTopic2Send();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        rabbitProducer.topicTopic3Send();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
