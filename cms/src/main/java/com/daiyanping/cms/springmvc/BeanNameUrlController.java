package com.daiyanping.cms.springmvc;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName BeanNameUrlController
 * @Description 在spring 3.1以前需通过如下方式实现Controller层
 * @Author daiyanping
 * @Date 2019-08-14
 * @Version 0.1
 */
@Component("/bean-name/test")
public class BeanNameUrlController implements Controller {


    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");
        return modelAndView;
    }
}
