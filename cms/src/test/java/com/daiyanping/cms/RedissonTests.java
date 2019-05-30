package com.daiyanping.cms;

import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.redisson.RedissonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName RedissonTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-30
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = {RedisConfig.class, MybatisMapperScanTest.class})
@ContextConfiguration(classes = {RedissonConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class RedissonTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("testdddd", "hhhhhh");
    }

}
