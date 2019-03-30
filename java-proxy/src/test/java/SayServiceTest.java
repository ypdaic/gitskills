import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.hamcrest.core.Is;
import org.junit.Test;
import service.ISayService;
import service.UserMapper;
import service.impl.ZhangShanServiceImpl;
import service.impl.ZhangShanServiceImplProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName SayServiceTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-03-11
 * @Version 0.1
 */
public class SayServiceTest {

	/**
	 * 静态代理
	 */
	@Test
	public void test1() {
		ISayService zhangShanService = new ZhangShanServiceImpl();
		ZhangShanServiceImplProxy zhangShanServiceImplProxy = new ZhangShanServiceImplProxy();
		zhangShanServiceImplProxy.setSayService(zhangShanService);
		zhangShanServiceImplProxy.sayHello();

	}

	/**
	 * jdk动态代理，相对于静态代理，我们并没有为此创建ZhangShanServiceImplProx这个类
	 * Proxy这个类会，并实例化创建该接口的实例字节码
	 * 这也是动态代理与静态代理最大的区别
	 * jdk动态代理只能代理实现了接口的类
	 */
	@Test
	public void test2() {
		ISayService zhangShanService = new ZhangShanServiceImpl();

		ISayService sayService = (ISayService) Proxy.newProxyInstance(ISayService.class.getClassLoader(),
				new Class[] {ISayService.class},
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("你好");
						return method.invoke(zhangShanService, args);

					}
				});
		sayService.sayHello();
	}

	/**
	 * cglib动态代理，可以不使用接口就可以进行代理
	 */
	@Test
	public void test3() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(LiShi.class);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
				System.out.println("你好");
				return methodProxy.invokeSuper(o, objects);
			}
		});
		LiShi liShi = (LiShi) enhancer.create();
		liShi.sayHello();

		String s = LiShi.class.toString();
		System.out.println(s);

	}

	/**
	 * 代理工具方法，可以为任意接口生成任意实例代理，该方式使用在了mybatis的Mapper接口映射Mapper.xml中，具体在MapperProxyFactory
	 * @param tClass
	 * @param invocationHandler
	 * @param <T>
	 * @return
	 */
	public <T> T test4(Class<T> tClass, InvocationHandler invocationHandler) {
		T t = (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[] {tClass},invocationHandler);
		return t;
	}

	@Test
	public void test6() {
		UserMapper userMapper = test4(UserMapper.class, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("查询所有");
				return null;
			}
		});
		userMapper.getAll();
	}

	@Test
	public void test5() {
		ISayService sayService = test4(ISayService.class, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("你好");
				return null;
			}
		});
		sayService.sayHello();
	}

	@Test
	public void test7() throws NoSuchMethodException {
		Class<UserMapper> userMapperClass = UserMapper.class;
		Method getAll = userMapperClass.getMethod("getAll", null);
		Class<?> declaringClass = getAll.getDeclaringClass();
		if (declaringClass.equals(userMapperClass)) {
			System.out.println("相等");
		}
		String name = UserMapper.class.getName();
		System.out.println(name);
	}

}
