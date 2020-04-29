package com.daiyanping.springcloud.provider.controller;


import com.daiyanping.springcloud.common.base.BaseController;
import com.daiyanping.springcloud.provider.entity.User;
import com.daiyanping.springcloud.provider.service.IUserService;
import com.daiyanping.springcloud.vo.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User user = userService.addUser(userDto);
        return user;
    }

    @PostMapping(value = "/deleteUser")
    public User deleteUser(@RequestBody UserDto userDto) {
        userService.deleteUser(userDto);
        return null;
    }

    @PostMapping(value = "/getUser")
    public User getUser(@RequestBody UserDto userDto) {
        User user = userService.getUser(userDto);
        return user;
    }

}

