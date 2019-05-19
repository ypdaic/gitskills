package com.daiyanping.cms.async;

import lombok.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncService implements IAsyncService{

	private boolean preserveTargetClass = true;

	//@Async，方法使用该注解，表示该方法以异步的方式执行
	@Async
//	@Transactional
	@Override
	public void async() {
		try {

			System.out.println("异步执行");
			int a = 1/0;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


}
