package com.daiyanping.cms.springmvc;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/html")
@Controller
public class HtmlController {

	@GetMapping("/upload")
	public String sayHello() {
		return "upload";
	}

}
