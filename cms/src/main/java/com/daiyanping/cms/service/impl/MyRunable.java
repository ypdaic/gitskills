package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName MyRunable
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-05
 * @Version 0.1
 */
@Service
public class MyRunable implements Runnable {

    @Autowired
    @Qualifier("service1")
    IUserService userService;

    @Override
    public void run() {
        test();
    }

    @Transactional(rollbackFor = Exception.class)
    public void test() {
        userService.getAll();
    }

}
