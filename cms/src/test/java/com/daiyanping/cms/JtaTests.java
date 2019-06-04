package com.daiyanping.cms;

import com.daiyanping.cms.DB.DBAspect;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.jta.JtaTransactionConfig;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * @ClassName JtaTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-04
 * @Version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {JtaTransactionConfig.class})
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//开启缓存 如果想要支持事物，缓存必须比事物后加载，这就是设置order，缓存的大于事物的order，这个order会用于后续advisors的排序，所以要想支持事物，缓存的调用链必须在事物的调用链中
//否则事物都结束了，即使缓存开启了事物支持，也是无效的
//@EnableCaching(order = 2)
@EnableTransactionManagement
public class JtaTests {

    //@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
    @Autowired
    @Qualifier("service1")
    private IUserService userService;

    /**
     * 验证动态数据源的使用
     */
    @Test
    public void test() {
        User userById = userService.getUserById("1");
        System.out.println(userById);

    }

    /**
     * 验证动态数据源的使用
     */
    @Test
    public void test2() {
        User user = new User();
        user.setAge(20);
        user.setId(1);
        user.setName("jta");
        userService.updateById(user);


    }

}
