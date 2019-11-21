package com.daiyanping.cms.jta;

import com.daiyanping.cms.entity.User;
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
 * @ClassName JtaWithAtomikosTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-04
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {JtaTransactionConfig3.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, RedissonAutoConfiguration.class, RedisAutoConfiguration.class})
//开启缓存 如果想要支持事物，缓存必须比事物后加载，这就是设置order，缓存的大于事物的order，这个order会用于后续advisors的排序，所以要想支持事物，缓存的调用链必须在事物的调用链中
//否则事物都结束了，即使缓存开启了事物支持，也是无效的
//@EnableCaching(order = 2)
@EnableTransactionManagement
public class JtaWithAtomikosTests {

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service5")
    private IUserService userService5;

    @Autowired
    @Qualifier("service6")
    private IUserService userService6;

    @Autowired
    TransactionTemplate transactionTemplate;

    /**
     * jdbcTemplate jta支持
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void test5() {
        User user = new User();
        user.setAge(80);
        user.setId(78);
        user.setPassword("dsfsdf");
        user.setName("jt21");


        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                userService5.addUser(user);
                userService6.addUser(user);
            }
        });



    }

}
