package com.daiyanping.cms;

import com.daiyanping.cms.service.IUserService;
import com.daiyanping.cms.service.impl.UserServiceImpl;
import com.daiyanping.cms.service.impl.UserServiceImpl4;
import org.junit.Test;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionTests {

	/**
	 * getDeclaredMethods方法只会获取本类中的所有方法包括public，protected，default，private
	 * ，但不会获取父类的方法。父接口的方法是default修饰的
	 * 也获取不到
	 */
	@Test
	public void test() {
		Class<UserServiceImpl> userServiceClass = UserServiceImpl.class;
		Method[] declaredMethods = userServiceClass.getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			System.out.println(declaredMethods[i].getName());

		}
	}

	/**
	 * getDeclaredMethods方法只会获取本类中的所有方法包括public，protected，default，private
	 * ，但不会获取父类的方法。父接口的方法是default修饰的
	 * 也获取不到
	 */
	@Test
	public void test2() {
		Class<SqlSessionDaoSupport> sqlSessionDaoSupportClass = SqlSessionDaoSupport.class;
		Method[] declaredMethods = sqlSessionDaoSupportClass.getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			System.out.println(declaredMethods[i].getName());

		}
	}

	/**
	 * getDeclaredMethods方法会获取接口中的所有方法包括使用default,static修饰的
	 */
	@Test
	public void test3() {
		Class<IUserService> iUserServiceClass = IUserService.class;
		Method[] declaredMethods = iUserServiceClass.getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			System.out.println(declaredMethods[i].getName());

		}
	}

	/**
	 * getDeclaredMethods方法只会获取本类中的所有方法包括public，protected，default，private
	 */
	@Test
	public void test4() {
		Class<UserServiceImpl4> userServiceImpl4Class = UserServiceImpl4.class;
		Method[] declaredMethods = userServiceImpl4Class.getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			System.out.println(declaredMethods[i].getName());

		}
	}

	/**
	 * getInterfaces方法只会获取本类实现的接口，其父类实现的接口获取不到，需要递归获取
	 */
	@Test
	public void test5() {
		Class<UserServiceImpl> userServiceClass = UserServiceImpl.class;
		Class<?>[] interfaces = userServiceClass.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			System.out.println(interfaces[i].getName());

		}
	}

	/**
	 * getInterfaces方法只会获取本类实现的接口，其父类实现的接口获取不到，需要递归获取
	 */
	@Test
	public void test6() {
		Class<IUserService> iUserServiceClass = IUserService.class;
		Method[] methods = iUserServiceClass.getMethods();
		for (int j = 0; j < methods.length; j++) {
			int modifiers = methods[j].getModifiers();
			System.out.println(modifiers);
			boolean anAbstract = Modifier.isAbstract(modifiers);
			System.out.println(anAbstract);
		}


	}
}
