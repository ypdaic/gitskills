package com.daiyanping.cms.springioc;


import com.daiyanping.cms.springioc.beanFactoryPostProcessor.BeanFatoryPostProcessorConfig;
import com.daiyanping.cms.springioc.importBeanDefinitionRegistrar.ImportBeanDefinitionRegistrarConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith,@SpringBootTest,@ContextConfiguration这三个注解，测试springboot项目需要用到
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration注解，加载我们指定的配置类
@ContextConfiguration(classes = {ImportBeanDefinitionRegistrarConfig.class})
//@EnableAutoConfiguration注解用于开启自动配置
// 自动配置提供，由于我们不连数据库，就需要排除springjdbc自动配置
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class, XADataSourceAutoConfiguration.class, TransactionAutoConfiguration.class, RabbitAutoConfiguration.class,
        RedissonAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class ImportBeanDefinitionRegistrarTests {

    @Autowired
    @Qualifier("test")
    private Object object;

    @Test
    public void test() {
        System.out.println(object);
    }
}
