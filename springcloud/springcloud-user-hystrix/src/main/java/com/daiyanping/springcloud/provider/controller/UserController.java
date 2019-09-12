package com.daiyanping.springcloud.provider.controller;


import com.daiyanping.springcloud.Product;
import com.daiyanping.springcloud.common.base.BaseController;
import com.daiyanping.springcloud.provider.entity.User;
import com.daiyanping.springcloud.provider.service.IUserService;
import com.daiyanping.springcloud.vo.UserDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author daiyanping
 * @since 2019-08-03
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    IUserService userService;

    @PostMapping(value = "/addUser")
    public User addUser(@RequestBody UserDto userDto) {
        log.trace("日志输出 trace");
        log.debug("日志输出 debug");
        log.info("日志输出 info");
        log.warn("日志输出 warn");
        log.error("日志输出 error");
        User user = userService.addUser(userDto);
        return user;
    }

    @GetMapping(value = "/getUser")
    @HystrixCommand(fallbackMethod = "getFallback")
    public User getUser() {
        User user = userService.getById(1);
        return user;
    }

    public User getFallback(){
        User user = new User();
        user.setName("HystrixName");
        return user;
    }

}

