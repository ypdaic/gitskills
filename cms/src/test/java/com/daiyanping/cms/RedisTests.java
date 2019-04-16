package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-15
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {RedisConfig.class, MybatisMapperScanTest.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//开启缓存
@EnableCaching
public class RedisTests {

    @Autowired
    private RedisTemplate<String, List<User>> redisTemplate;

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service1")
    private UserServiceImpl userService;

    private final int threadCount = 1;
    private CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    @Test
    public void test() {
        List<User> users = userService.getAll();
        redisTemplate.boundValueOps("user").set(users, 10, TimeUnit.MINUTES);
        List<User> list = redisTemplate.boundValueOps("user").get();
        System.out.println(list);

        redisTemplate.boundListOps("user2").leftPush(users);
        List<User> user2 = redisTemplate.boundListOps("user2").leftPop();
        redisTemplate.boundListOps("user2").leftPush(user2, users);
//        System.out.println(user2);

    }

    /**
     * 使用缓存解决数据库压力，比如突发大量请求，如果都去请求数据库，就会将
     * 数据库搞挂掉，需要使用缓存解决该问题
     */
    @Test
    public void test2() {
//        for (int i = 0; i < threadCount; i++) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    countDownLatch.countDown();
//                    try {
//                        countDownLatch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    User userById = userService.getUserById("1");
//                }
//            });
//
//            thread.start();
//
//        }
//
//        try {
//            Thread.sleep(1000 * 60 * 5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        User userById = userService.getUserById("1");
    }
}
