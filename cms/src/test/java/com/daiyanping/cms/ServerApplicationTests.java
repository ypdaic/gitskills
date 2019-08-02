package com.daiyanping.cms;

import com.daiyanping.cms.DB.DBAspect;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.mapper.UserMapper;
import com.daiyanping.cms.redis.RedisConfig;
import com.daiyanping.cms.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
		System.out.println(bean2.getName());

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


	/**
	 * 初略的验证了ImportBeanDefinitionRegistrar接口的使用结合ImportSelector，可以这样理解，在导入外部jar中的Configuration配置类的同时，加载ImportBeanDefinitionRegistrar实现
	 * 类中指定的类，并将其加载到spring容器中（加载指定的类可以是我们手动指定的BeanDefinition,手动指定的包路径，然后去扫描加载）
	 *
	 */
	@Test
	public void test1() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		ConfigurableEnvironment environment = annotationConfigApplicationContext.getEnvironment();
		environment.setActiveProfiles("dev");
		//这里以AppConfig为spring上下文，该配置类中并没有配置任何bean，我们为其导入了ImportSelector的实现类，就会去加载实现类中，具体要
		//加载的配置类，这里我们加载了ConfigurationTest这个配置类，该配置类中配置了UseDao这个bean
		annotationConfigApplicationContext.register(AppConfig.class);
		annotationConfigApplicationContext.refresh();
		//我们可以在spring上下文中访问到这个bean
		UserMapper bean = annotationConfigApplicationContext.getBean(UserMapper.class);
		System.out.println(bean);
		System.out.println(bean.getUser());
	}

	/**
	 * ImportBeanDefinitionRegistrar不结合ImportSelector，可以直接使用import导入
	 * 初略的验证使用BeanDefinitionBuilder结合ImportBeanDefinitionRegistrar进行bean的注入
	 */
	@Test
	public void test2() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(BeanDefinitionBuilderTest.class);
		annotationConfigApplicationContext.refresh();
		UserMapper bean = (UserMapper) annotationConfigApplicationContext.getBean("test");
		System.out.println(bean);
		User user = bean.getUser();
		System.out.println(user);
	}

	/**
	 * ImportBeanDefinitionRegistrar不结合ImportSelector，可以直接使用import导入
	 * 初略的验证使用ClassPathBeanDefinitionScanner结合ImportBeanDefinitionRegistrar进行bean的注入
	 */
	@Test
	public void test3() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(ClassPathBeanDefinitionScannerTest.class);
		annotationConfigApplicationContext.refresh();
		UserMapper bean = (UserMapper) annotationConfigApplicationContext.getBean(UserMapper.class);
		System.out.println(bean);
	}

	/**
	 * 验证FactoryBean的用法，继承了FactoryBean后，实现getObject方法，该方法返回的对象会被注入到spring容器中
	 */
	@Test
	public void test4() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(FactoryBeanConfig.class);
		annotationConfigApplicationContext.refresh();
		User bean = annotationConfigApplicationContext.getBean(User.class);
		System.out.println(bean);
	}

	/**
	 * 验证EnableConfigurationProperties注解加载properties,并注入到spring容器中
	 */
	@Test
	public void test5() {

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(AppConfig.class);
		annotationConfigApplicationContext.refresh();
		ConfigurationPropertiesTest2 bean = annotationConfigApplicationContext.getBean(ConfigurationPropertiesTest2.class);
		System.out.println(bean.getName());

		UserDao bean1 = annotationConfigApplicationContext.getBean(UserDao.class);
		System.out.println(bean1);

		AppConfig bean3 = annotationConfigApplicationContext.getBean(AppConfig.class);
		System.out.println(bean3);
		DataSourceAutoConfiguration bean2 = annotationConfigApplicationContext.getBean(DataSourceAutoConfiguration.class);
		System.out.println(bean2);
	}

	/**
	 * 验证mybatis分页插件的使用
	 */
	@Test
	public void test6() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(MybatisMapperScanTest.class);
		annotationConfigApplicationContext.refresh();
		UserDao bean = annotationConfigApplicationContext.getBean(UserDao.class);
		PageHelper.startPage(1, 2);
		List<User> allUser = bean.getAllUser();
		PageInfo<User> userPageInfo = new PageInfo<>(allUser);
		System.out.println(userPageInfo);
	}

	/**
	 * 验证动态数据源的使用
	 */
	@Test
	public void test7() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(MybatisMapperScanTest.class);
		annotationConfigApplicationContext.refresh();
		DBAspect bean = annotationConfigApplicationContext.getBean(DBAspect.class);
		System.out.println(bean);
		IUserService userService = (IUserService) annotationConfigApplicationContext.getBean("service1");
		PageHelper.startPage(1, 2);
		List<User> allUser = userService.getAll();
		PageInfo<User> userPageInfo = new PageInfo<>(allUser);
		System.out.println(userPageInfo.getList().get(0));

		IUserService userService2 = (IUserService) annotationConfigApplicationContext.getBean("service2");
		PageHelper.startPage(1, 2);
		List<User> allUser2 = userService2.getAll();
		PageInfo<User> userPageInfo2 = new PageInfo<>(allUser2);
		System.out.println(userPageInfo2.getList().get(0));
	}

	/**
	 * 验证service方法自己内部相互调用是否可以切换数据源
	 */
	@Test
	public void test8() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(MybatisMapperScanTest.class);
		annotationConfigApplicationContext.refresh();
		DBAspect bean = annotationConfigApplicationContext.getBean(DBAspect.class);
		System.out.println(bean);
		IUserService userService = (IUserService) annotationConfigApplicationContext.getBean("service1");
		User user = new User();
		user.setId(1);
		user.setName("daiyanping2");
		userService.updateById(user);
	}

	/**
	 * 验证spring事物级别Propagation.MANDATORY的作用
	 * 其作用就是,必须在一个事务中运行。也就是说，他只能被一个父事务调用。否则，他就要抛出异常
	 * 如果上层调用没有事物，这里抛异常是没法回滚的
	 */
	@Test
	public void test9() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(MybatisMapperScanTest.class);
		annotationConfigApplicationContext.refresh();
		DBAspect bean = annotationConfigApplicationContext.getBean(DBAspect.class);
		System.out.println(bean);
		IUserService userService = (IUserService) annotationConfigApplicationContext.getBean("service1");
		User user = new User();
		user.setId(1);
		user.setName("daiyanping87");
		user.setAge(28);
		userService.updateById(user);
	}

	/**
	 *
	 */
	@Test
	public void test10() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(RedisConfig.class);
		annotationConfigApplicationContext.refresh();
		annotationConfigApplicationContext.close();
	}
	/**
	 *
	 */
	@Test
	public void test11() {
		int a = (1 << 16) - 1;
		System.out.println(a);
		int b = a;
		int i = 65536 & a;
		System.out.println(i);


		System.out.println(0 >>> 16);

		ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
		ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
		ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
		readLock.lock();
		readLock.lock();
		readLock.unlock();
		readLock.unlock();
	}




}
