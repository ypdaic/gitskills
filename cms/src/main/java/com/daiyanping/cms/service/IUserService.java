package com.daiyanping.cms.service;

import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName IUserService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
public interface IUserService{

    List<User> getAll();
}
