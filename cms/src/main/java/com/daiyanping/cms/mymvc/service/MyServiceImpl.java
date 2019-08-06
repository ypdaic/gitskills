package com.daiyanping.cms.mymvc.service;

import com.daiyanping.cms.mymvc.annotation.MyService;

/**
 * @ClassName MyServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-06
 * @Version 0.1
 */
@MyService("myServiceImpl")
public class MyServiceImpl implements IMyService{

    @Override
    public String say(String test) {
        return "手写mvc：" + test;
    }
}
