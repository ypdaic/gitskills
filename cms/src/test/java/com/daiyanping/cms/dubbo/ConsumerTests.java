package com.daiyanping.cms.dubbo;

import com.daiyanping.cms.dubbo.consumer.ConsumerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName ConsumerTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-24
 * @Version 0.1
 */
//@RunWith,@SpringBootTest,@ContextConfiguration这三个注解，测试springboot项目需要用到
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration注解，加载我们指定的配置类
@ContextConfiguration(classes = {ConsumerConfig.class})
//@EnableAutoConfiguration注解用于开启自动配置，@EnableAsync注解开启的异步功能使用的线程池就是由
// 自动配置提供，由于我们不连数据库，就需要排除springjdbc自动配置
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class, RedissonAutoConfiguration.class})
public class ConsumerTests {

    @Autowired
    ConsumerService consumerService;

    @Test
    public void test() {
        consumerService.say();
    }
}
