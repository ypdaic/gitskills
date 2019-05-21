package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.redis.RedisTemplateForTransactional;
import com.daiyanping.cms.service.IUserService;
import com.daiyanping.cms.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Set;
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
//@ContextConfiguration(classes = {RedisConfig.class, MybatisMapperScanTest.class})
@ContextConfiguration(classes = {MybatisMapperScanTest.class, RedisConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//开启缓存 如果想要支持事物，缓存必须比事物后加载，这就是设置order，缓存的大于事物的order，这个order会用于后续advisors的排序，所以要想支持事物，缓存的调用链必须在事物的调用链中
//否则事物都结束了，即使缓存开启了事物支持，也是无效的
@EnableCaching(order = 2)
@EnableTransactionManagement
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service1")
    private IUserService userService;

    private final int threadCount = 1;
    private CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    @Autowired
    RedisTemplateForTransactional redisTemplateForTransactional;

    @Test
    public void test() {
        List<User> users = userService.getAll();
        redisTemplate.boundValueOps("user").set(users, 10, TimeUnit.MINUTES);
        List<User> list = (List<User>) redisTemplate.boundValueOps("user").get();
        System.out.println(list);

        redisTemplate.boundListOps("user2").leftPush(users);
        List<User> user2 = (List<User>) redisTemplate.boundListOps("user2").leftPop();
        redisTemplate.boundListOps("user2").leftPush(user2, users);
//        System.out.println(user2);

    }

    @Test
    public void test3() {
//        Set<String> test = redisTemplate.keys("test");
//        System.out.println(test);
        Boolean delete = redisTemplate.delete("test");
        System.out.println(delete);
//        redisTemplate.boundValueOps("test:2").set("sssss");

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
        User userById = userService.getUser("1");
    }

    /**
     * 测试RedisTemplate的事物功能
     *
     */
    @Test
    public void test4() {
//        redisTemplateForTransactional.test();
        redisTemplateForTransactional.test2();
    }
}
