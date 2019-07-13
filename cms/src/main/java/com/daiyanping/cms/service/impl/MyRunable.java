package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName MyRunable
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-05
 * @Version 0.1
 */
@Component
public class MyRunable {

    @Autowired
    @Qualifier("service1")
    IUserService userService;


    public void run() {
        test();
    }

    @Transactional(rollbackFor = Exception.class)
    public void test() {
        List<User> all = userService.getAll();
        System.out.println(all);
    }

}
