package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.DB.DB;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("service4")
@DB(DB = DBTypeEnum.TEST2)
public class UserServiceImpl4 implements IUserService {

    @Autowired
    @Qualifier("jdbcTemplate2")
    JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void updateById(User user) {

    }

    @Override
    public void updateByName(User user) {

    }

    @Override
    public void updateByAge(User user) {

    }

    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public void addUser(User user) {
        jdbcTemplate.execute("insert into user (id,name,password,age) values (?,?,?,?)", (PreparedStatementCallback<Object>) (ps) -> {
            ps.setObject(1, user.getId());
            ps.setObject(2, user.getName());
            ps.setObject(3, user.getPassword());
            ps.setObject(4, user.getAge());

            return ps.execute();
        });

        int a = 1/0;
    }
}
