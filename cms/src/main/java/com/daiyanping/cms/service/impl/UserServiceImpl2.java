package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.DB.DB;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.daiyanping.cms.DB.DBTypeEnum.TEST2;

/**
 * @ClassName UserServiceImpl2
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-04
 * @Version 0.1
 */
@Service("service2")
@DB(DB = TEST2)
public class UserServiceImpl2 implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAll() {
        return userDao.getAllUser();
    }

    public void updateById(User user) {
        userDao.updateById(user);
        updateByName(user);
    }

    public void updateByName(User user) {
        userDao.updateByName(user);
    }
}
