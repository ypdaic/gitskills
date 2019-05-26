package com.daiyanping.cms.springmvc;

import com.alibaba.fastjson.JSONObject;
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
}
