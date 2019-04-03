package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.DB.DB;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.service.IUserService;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@DB(DB = DBTypeEnum.TEST)
@Service
public class UserServiceImpl extends SqlSessionDaoSupport implements IUserService {

    @Autowired
    private UserDao userDao;
}
