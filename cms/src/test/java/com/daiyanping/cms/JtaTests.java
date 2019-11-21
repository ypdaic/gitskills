package com.daiyanping.cms;

import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.jta.DataSourceConfig;
import com.daiyanping.cms.jta.JtaTransactionConfig;
import com.daiyanping.cms.jta.JtaTransactionConfig2;
import com.daiyanping.cms.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @ClassName JtaTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-04
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {DataSourceConfig.class, JtaTransactionConfig2.class, JtaTransactionConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, RedissonAutoConfiguration.class, RedisAutoConfiguration.class})
//开启缓存 如果想要支持事物，缓存必须比事物后加载，这就是设置order，缓存的大于事物的order，这个order会用于后续advisors的排序，所以要想支持事物，缓存的调用链必须在事物的调用链中
//否则事物都结束了，即使缓存开启了事物支持，也是无效的
//@EnableCaching(order = 2)
@EnableTransactionManagement
public class JtaTests {

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service1")
    private IUserService userService;

    @Autowired
    @Qualifier("service2")
    private IUserService userService2;

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service3")
    private IUserService userService3;

    @Autowired
    @Qualifier("service4")
    private IUserService userService4;

    @Autowired
    TransactionTemplate transactionTemplate;




    /**
     * 验证动态数据源的使用
     */
    @Test
    public void test() {
        User userById = userService.getUserById("1");
        System.out.println(userById);

    }

    /**
     *
     */
    @Test
    @Transactional
    public void test2() {
        User user = new User();
        user.setAge(80);
        user.setId(1);
        user.setName("jt21");
        Thread thread1 = new Thread(() -> {
            userService.updateById(user);
        });
        Thread thread2 = new Thread(() -> {
            userService.updateByName(user);
    });
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * mybatis-spring 不支持jta
     */
    @Test
    @Transactional
//    @Rollback(value = false)
    public void test3() {
        User user = new User();
        user.setAge(80);
        user.setId(78);
        user.setPassword("dsfsdf");
        user.setName("jt21");

        userService.addUser(user);


        userService2.addUser(user);


    }

    /**
     * jdbcTemplate jta支持
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void test4() {
        User user = new User();
        user.setAge(80);
        user.setId(78);
        user.setPassword("dsfsdf");
        user.setName("jt21");


        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                userService3.addUser(user);
                userService4.addUser(user);
            }
        });



    }

}
