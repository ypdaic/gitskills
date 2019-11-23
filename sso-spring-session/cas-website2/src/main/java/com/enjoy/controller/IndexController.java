package com.enjoy.controller;

import com.enjoy.utils.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Controller
public class IndexController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        UserForm userInfo = (UserForm) request.getSession().getAttribute("userinfo");
        if (Objects.isNull(userInfo)) {

            userInfo = new UserForm();
            userInfo.setUsername("daiyanping");
            userInfo.setPassword("password");
            request.getSession().setAttribute("userinfo",userInfo);
        }

        modelAndView.setViewName("index");
        modelAndView.addObject("user", userInfo);
        return modelAndView;
    }
}
