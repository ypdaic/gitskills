package com.daiyanping.cms;

import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.mysql.jdbc.Driver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServerApplicationTests {

	@Test
	public void contextLoads() {
//		AnnotationConfigApplicationContext ctx=new AnnotationConfigApplicationContext();
//		ctx.getEnvironment().setActiveProfiles("dev");
//		ctx.register(ConfigurationTest.class);
//		ctx.refresh();

//		UserDao bean = ctx.getBean(UserDao.class);
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();

		//设置配置环境
		ConfigurableEnvironment environment1 = classPathXmlApplicationContext.getEnvironment();
		environment1.setActiveProfiles("dev");
		classPathXmlApplicationContext.setConfigLocation("spring.xml");
		classPathXmlApplicationContext.refresh();
		//没有指定具体注入bean的名称，只能使用Class来获取以及注入的bean
		//获取使用Configuration注解注入的bean
		UserDao userDao = (UserDao) classPathXmlApplicationContext.getBean(UserDao.class);
		userDao.getAllUser();

		//获取基于Import注解注入的bean
		User user = (User) classPathXmlApplicationContext.getBean(User.class);
		System.out.println(user.getClass());

		//获取配置了ConfigurationProperties注解的bean
		ConfigurationPropertiesTest bean = (ConfigurationPropertiesTest) classPathXmlApplicationContext.getBean(ConfigurationPropertiesTest.class);
		System.out.println(bean.getName());

		//获取spring容器中的BeanFactory
		BeanFactoryAwareTest bean1 = classPathXmlApplicationContext.getBean(BeanFactoryAwareTest.class);
		BeanFactory beanFactory = bean1.getBeanFactory();
		User bean2 = beanFactory.getBean(User.class);
		System.out.println(bean2.getUsername());

		//获取已经注入spring容器的bean在spring容器中的名称
		//名称默认是类名首字母小写
		BeanNameAwareTest bean3 = classPathXmlApplicationContext.getBean(BeanNameAwareTest.class);
		System.out.println(bean3.getName());

		//获取bean的类加载器
		BeanClassLoaderAwareTest bean4 = classPathXmlApplicationContext.getBean(BeanClassLoaderAwareTest.class);
		System.out.println(bean4.getClassLoader());

		//获取spirng容器的resourceloader
		ResourceLoaderAwareTest bean5 = classPathXmlApplicationContext.getBean(ResourceLoaderAwareTest.class);
		ResourceLoader resourceLoader = bean5.getResourceLoader();
		Resource resource = resourceLoader.getResource("application.yml");
		if (resource.exists()) {
			System.out.println("获取spring容器的resource");
		}

		//获取spring应用的环境参数
		EnvironmentAwareTest bean6 = classPathXmlApplicationContext.getBean(EnvironmentAwareTest.class);
		Environment environment = bean6.getEnvironment();
		System.out.println(environment.toString());

		classPathXmlApplicationContext.close();
	}

	@Test
	public void Test() {
		try {

			SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
			SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
			simpleDriverDataSource.setDriver(new Driver());

			sqlSessionFactoryBean.setDataSource(simpleDriverDataSource);
		} catch (Exception e) {

		}
	}
}
