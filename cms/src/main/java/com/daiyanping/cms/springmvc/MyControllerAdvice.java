package com.daiyanping.cms.springmvc;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

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

	@ModelAttribute("test")
	public String modelAttribute(ModelAndView modelAndView) {
		return "modeAndView测试";
	}
}
