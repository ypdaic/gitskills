package com.daiyanping.cms;

import com.daiyanping.cms.async.AsyncConfig;
import com.daiyanping.cms.async.AsyncService;
import com.daiyanping.cms.async.IAsyncService;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.scheduledTask.ScheduledTaskConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@RunWith,@SpringBootTest,@ContextConfiguration这三个注解，测试springboot项目需要用到
@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration注解，加载我们指定的配置类
@ContextConfiguration(classes = {AsyncConfig.class})
//@EnableAutoConfiguration注解用于开启自动配置，@EnableAsync注解开启的异步功能使用的线程池就是由
// 自动配置提供，由于我们不连数据库，就需要排除springjdbc自动配置
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//@EnableAsync开启异步功能
@EnableAsync
public class AsyncTests {

	@Autowired
	private IAsyncService asyncService;

	@Test
	public void test() {
		asyncService.async();
		try {
			Thread.sleep(1000 * 60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
