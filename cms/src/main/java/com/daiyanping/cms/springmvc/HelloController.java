package com.daiyanping.cms.springmvc;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/hello")
@RestController
public class HelloController {

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
