package service.impl;

import service.ISayService;

/**
 * ZhangShanServiceImpl代理类,这种是静态代理。
 *
 * 总结：其实这里做的事情无非就是，创建一个代理类ZhangShanServiceImplProxy，继承了ISayService接口并实现了其中的方法。
 * 只不过这种实现特意包含了目标对象的方法，正是这种特征使得看起来像是“扩展”了目标对象的方法。
 * 假使代理对象中只是简单地对sayHello方法做了另一种实现而没有包含目标对象的方法，也就不能算作代理模式了。
 * 所以这里的包含是关键。
 *
 * 缺点：这种实现方式很直观也很简单，但其缺点是代理对象必须提前写出，如果接口层发生了变化，代理对象的代码也要进行维护。
 * 如果能在运行时动态地写出代理对象，不但减少了一大批代理类的代码，也少了不断维护的烦恼，不过运行时的效率必定受到影响。
 * 这种方式就是接下来的动态代理。
 *
 */
public class ZhangShanServiceImplProxy implements ISayService {

	private ISayService sayService;

	@Override
	public void sayHello() {
		System.out.println("你好");
		sayService.sayHello();
	}

	public ISayService getSayService() {
		return sayService;
	}

	public void setSayService(ISayService sayService) {
		this.sayService = sayService;
	}

}