package com.daiyanping.cms.service.impl;

import com.daiyanping.cms.DB.AopProxyContext;
import com.daiyanping.cms.DB.DB;
import com.daiyanping.cms.DB.DBTypeEnum;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.IUserService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 验证service方法自己内部相互调用不可以切换数据源，想要切数据源，需要借助自己实现的AopProxyContext.getAopProxy();类
     * @param user
     */
    public void updateById(User user) {
        userDao.updateById(user);
        Object aopProxy = AopProxyContext.getAopProxy();
        IUserService aopProxy1 = (IUserService) aopProxy;
        aopProxy1.updateByName(user);
    }

    @DB(DB = DBTypeEnum.TEST2)
    public void updateByName(User user) {
        userDao.updateByName(user);
    }


}
