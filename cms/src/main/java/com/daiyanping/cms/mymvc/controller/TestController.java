package com.daiyanping.cms.mymvc.controller;

import com.daiyanping.cms.mymvc.annotation.MyController;
import com.daiyanping.cms.mymvc.annotation.MyQualifier;
import com.daiyanping.cms.mymvc.annotation.MyRequestMapping;
import com.daiyanping.cms.mymvc.annotation.MyRequestParameter;
import com.daiyanping.cms.mymvc.service.IMyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-06
 * @Version 0.1
 */
@MyController
@MyRequestMapping("/test")
public class TestController {

    @MyQualifier("myServiceImpl")
    IMyService myService;

    @MyRequestMapping("/say")
    public String say(HttpServletRequest request, HttpServletResponse response, @MyRequestParameter("test") String test) {
        String say = myService.say(test);
        return say;
    }
}
