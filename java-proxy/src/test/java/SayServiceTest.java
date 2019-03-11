import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.hamcrest.core.Is;
import org.junit.Test;
import service.ISayService;
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

		/*
			第一个参数，第二个参数，必须是接口实现类调用getClassLoader与getInterfaces
		 */
		ISayService sayService = (ISayService) Proxy.newProxyInstance(ZhangShanServiceImpl.class.getClassLoader(),
				ZhangShanServiceImpl.class.getInterfaces(),
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

	}
}
