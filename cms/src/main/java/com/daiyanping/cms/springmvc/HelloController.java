package com.daiyanping.cms.springmvc;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/hello")
@RestController
public class HelloController {

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	@PostMapping("/say")
	public JSONObject sayHello() {
		JSONObject jsonObject = new JSONObject();
		return jsonObject;
	}

	@GetMapping("/say2")
	public JSONObject sayHello2() {
		JSONObject jsonObject = new JSONObject();
		return jsonObject;
	}

	/**
	 * 使用WebAsyncTask进行异步，由AsyncTaskMethodReturnValueHandler处理
	 * @return
	 */
	@GetMapping("/say3")
	public WebAsyncTask<JSONObject> sayHello3() {
		WebAsyncTask<JSONObject> jsonObjectWebAsyncTask = new WebAsyncTask<JSONObject>(() -> {
//			Thread.sleep(1000 * 60);
			JSONObject jsonObject = new JSONObject();
			return jsonObject;
		});

		return jsonObjectWebAsyncTask;

	}

	/**
	 * 使用Callable进行异步，由CallableMethodReturnValueHandler处理
	 * @return
	 */
	@GetMapping("/say5")
	public Callable<JSONObject> sayHello5() {
		Callable<JSONObject> callable = new Callable<JSONObject>() {
			@Override
			public JSONObject call() throws Exception {
				return new JSONObject();
			}
		};
		return callable;
	}

	/**
	 * 使用DeferredResult进行异步,但是DeferredResult的结果需要我们在其他位置另外设置，由DeferredResultMethodReturnValueHandler处理
	 * @return
	 */
	@GetMapping("/say6")
	public DeferredResult<JSONObject> sayHello6() {
		DeferredResult<JSONObject> deferredResult = new DeferredResult<>();
		new Thread(() -> {

			deferredResult.onCompletion(() -> {
				System.out.println("执行完成后回调");
			});
			deferredResult.onError(this::onError);
			deferredResult.onTimeout(() -> {
				System.out.println("超时回调");
			});
			deferredResult.setResult(new JSONObject());

		}).start();

		return deferredResult;
	}

	/**
	 * 使用CompletableFuture进行异步,但是这个会被ResponseBodyEmitterReturnValueHandler进行解析，最后还是在ReactiveTypeHandler中
	 * 进行异步处理
	 * @return
	 */
	@GetMapping("/say7")
	public CompletableFuture<JSONObject> sayHello7() {
		CompletableFuture<JSONObject> completableFuture = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			JSONObject object = new JSONObject();
			return object;
		});
		return completableFuture;
	}

	/**
	 * 使用ListenableFuture进行异步,由DeferredResultMethodReturnValueHandler处理,当业务处理完后，会调用FutureTask的done方法，由子类实现
	 * ListenableFutureTask实现了该方法，其中会触发回调
	 * 进行异步处理
	 * @return
	 */
	@GetMapping("/say8")
	public ListenableFuture<JSONObject> sayHello8() {
		ListenableFuture<JSONObject> jsonObjectListenableFuture = taskExecutor.submitListenable(() -> {
			Thread.sleep(1000 * 20);
			JSONObject object = new JSONObject();
			return object;
		});
		return jsonObjectListenableFuture;

	}

	public void onError(Throwable t) {
		System.out.println("错误回调");
	}

	@GetMapping("/say4")
	public JSONObject sayHello4(@ModelAttribute("testInitBinder") String test) {

		JSONObject jsonObject = new JSONObject();
		System.out.println(test);
		return jsonObject;
	}

	@PostMapping("/say/error")
	public JSONObject sayError() {
		int a= 1/0;
		return null;
	}

	@PostMapping("/upload")
	public JSONObject upload(MultipartFile multipartFile) throws IOException {
		String originalFilename = multipartFile.getOriginalFilename();
		byte[] bytes = multipartFile.getBytes();
		System.out.println(bytes);
		return null;
	}

	//@Qualifier注解用于存在多个相同类型的bean时，注入指定名称的bean
	@Autowired
	@Qualifier("service1")
	private IUserService userService;

	/**
	 * 验证动态数据源的使用
	 */
	@PostMapping("/updateById")
	public void test2() {
		User user = new User();
		user.setAge(20);
		user.setId(1);
		user.setName("jta");
		userService.updateById(user);

	}

}
