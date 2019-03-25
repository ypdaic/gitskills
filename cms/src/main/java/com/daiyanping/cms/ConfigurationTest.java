package com.daiyanping.cms;

import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
//在4.2之前只支持导入配置类
//在4.2，@Import注解支持导入普通的java类,并将其声明成一个bean
@Import(com.daiyanping.cms.entity.User.class)
public class ConfigurationTest {

	@Bean(initMethod = "init",destroyMethod = "destroy")
	//根据不同的配置环境，获取不同的bean
	@Profile({"pro"})
	public UserDao getUserDao() {
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

	@Bean(initMethod = "init",destroyMethod = "destroy")
	//根据不同的配置环境，获取不同的bean
	@Profile({"dev"})
	public UserDao getUserDao2() {
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
