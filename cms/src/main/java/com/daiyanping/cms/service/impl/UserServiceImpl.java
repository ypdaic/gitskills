package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.DB.DB;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@DB(DB = DBTypeEnum.TEST)
@Service("service1")
public class UserServiceImpl extends SqlSessionDaoSupport implements IUserService {

    @Autowired
    private UserDao userDao;

    public List<User> getAll() {
        SqlSession sqlSession = this.getSqlSession();
        List<User> objects = sqlSession.selectList("com.daiyanping.cms.dao.UserDao.getAllUser");
        return objects;
    }

    /**
     * 父类属性注入
     * @param sqlSessionFactory
     */
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }


}
