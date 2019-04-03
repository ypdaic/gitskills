package com.daiyanping.cms;

import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
//在4.2之前只支持导入配置类
//在4.2，@Import注解支持导入普通的java类,并将其声明成一个bean
@Import(com.daiyanping.cms.entity.User.class)
@EnableConfigurationProperties(ConfigurationPropertiesTest2.class)
//加载DataSourceAutoConfiguration配置类后，再加载ConfigurationTest配置类

//非springboot 使用AutoConfigAfter是不起作用的,而且该注解并不能控制配置类是否加载，而是控制其加载顺序
@AutoConfigureAfter({DataSourceAutoConfiguration.class})

//@Conditional 注解可以控制某个类是否加载
//@Conditional(WindowCondition.class)
public class ConfigurationTest {

	@Bean
	//根据不同的配置环境，获取不同的bean
	@Profile({"pro"})
	public UserDao devUserDao() {
		System.out.println("生产环境");
		return new UserDao() {
			@Override
			public List<User> getAllUser() {
				return null;
			}

			public void init() {
				System.out.println("bean初始化之前调用");
			}

			public void destroy() {
				System.out.println("bean销毁后调用");
			}

		};
	}

	@Bean
	//根据不同的配置环境，获取不同的bean
//	@Profile({"dev"})
//	@Conditional(WindowCondition.class)
	public UserDao proUserDao() {
		System.out.println("开发环境");
		return new UserDao() {
			@Override
			public List<User> getAllUser() {
				return null;
			}

			public void init() {
				System.out.println("bean初始化之前调用");
			}

			public void destroy() {
				System.out.println("bean销毁后调用");
			}

		};
	}
}
