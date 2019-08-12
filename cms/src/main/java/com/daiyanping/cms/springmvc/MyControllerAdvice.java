package com.daiyanping.cms.springmvc;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截RestController的请求
 */
@RestControllerAdvice
public class MyControllerAdvice {

	@ExceptionHandler(value = Exception.class)
	public JSONObject exception(Exception exception, HttpServletRequest httpServletRequest) {
		exception.printStackTrace();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("error", "系统内部错误");

		return jsonObject;
	}

	/**
	 * 以键值对的形式塞到ModelAndViewContainer中
	 * @return
	 */
	@ModelAttribute("test")
	public String modelAttribute() {
		return "modeAndView测试";
	}

	@InitBinder("testInitBinder")
	public void initBinder() {

	}
}
