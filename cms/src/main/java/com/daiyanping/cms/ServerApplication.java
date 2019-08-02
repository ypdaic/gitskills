package com.daiyanping.cms;

import com.daiyanping.cms.springmvc.SpringMvcConfig;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootConfiguration
//开启自动配置，排除springjdbc自动配置
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, RedissonAutoConfiguration.class})
//@ComponentScan(basePackageClasses = {ScheduledTaskConfig.class, RedisConfig.class})
//@ComponentScan(basePackageClasses = {RedisConfig.class})
@ComponentScan(basePackageClasses = {SpringMvcConfig.class})
//@ComponentScan(basePackageClasses = {SpringMvcConfig.class})
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//开启缓存
@EnableCaching
//开启定时任务
@EnableScheduling
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}


