package service.impl;

import service.ISayService;

public class ZhangShanServiceImpl implements ISayService {

	@Override
	public void sayHello() {
		System.out.println("我是张三！");
	}
}
