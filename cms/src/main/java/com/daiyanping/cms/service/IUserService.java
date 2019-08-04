package com.daiyanping.cms.service;

import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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

    void updateById(User user);

    void updateByName(User user);

    void updateByAge(User user);

    User getUserById(String id);

    User getUser(String id);

    default void test(){};

    static void test2(){};

    void addUser(User user);
}
