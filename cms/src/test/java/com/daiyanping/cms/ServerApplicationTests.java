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


	/**
	 * 初略的验证了ImportSelector接口的使用，可以这样理解，就是可以导入外部jar中的Configuration配置类，使其生效
	 */
	@Test
	public void test() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		ConfigurableEnvironment environment = annotationConfigApplicationContext.getEnvironment();
		environment.setActiveProfiles("dev");
		//这里以AppConfig为spring上下文，该配置类中并没有配置任何bean，我们为其导入了ImportSelector的实现类，就会去加载实现类中，具体要
		//加载的配置类，这里我们加载了ConfigurationTest这个配置类，该配置类中配置了UseDao这个bean
		annotationConfigApplicationContext.register(AppConfig.class);
		annotationConfigApplicationContext.refresh();
		//我们可以在spring上下文中访问到这个bean
		UserDao bean = annotationConfigApplicationContext.getBean(UserDao.class);
		System.out.println(bean);
	}


}
