package com.daiyanping.cms;

import com.daiyanping.cms.async.AsyncConfig;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

//@RunWith,@SpringBootTest,@ContextConfiguration这三个注解，测试springboot项目需要用到
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration注解，加载我们指定的配置类
@ContextConfiguration(classes = {MybatisMapperScanTest.class})
//@EnableAutoConfiguration注解用于开启自动配置，@EnableAsync注解开启的异步功能使用的线程池就是由
// 自动配置提供，由于我们不连数据库，就需要排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//@EnableAsync开启异步功能
@EnableTransactionManagement
public class TransactionTests {

	@Autowired()
	@Qualifier("service1")
	private IUserService userService;

	@Autowired
    TransactionTemplate transactionTemplate;

	@Autowired
	UserDao userDao;

	@Test
	public void test() {
		userService.getAll();
	}

	/**
	 * 不使用@Transaction 注解的情况下，我们可以使用transactionTemplate进行事物处理
	 */
	@Test
	public void test2() {
		Object result = transactionTemplate.execute(status -> {
			List<User> allUser = userDao.getAllUser();
			return allUser;
		});

		System.out.println(result);
	}
}
