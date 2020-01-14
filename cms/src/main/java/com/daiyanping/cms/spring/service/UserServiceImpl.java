package com.daiyanping.cms.spring.service;

import com.daiyanping.cms.spring.bean.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private Student student;

//    @Autowired
//    private AccountService accountService;

    @Override
    public String queryUser(String userId) {
        return "xxx";
    }

    @Override
    public void addxx(String id) {

    }
}
