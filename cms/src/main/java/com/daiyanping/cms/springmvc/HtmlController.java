package com.daiyanping.cms.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/html")
@Controller
public class HtmlController {

	@GetMapping("/upload")
	public String sayHello(@ModelAttribute("testInitBinder") String test) {
		return "upload" + test ;
	}

	@GetMapping("/upload2")
	public ModelAndView sayHello2(ModelAndView modelAndView) {
		modelAndView.addObject("test", "test");
		return modelAndView ;
	}


}
