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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
     * Spring Cache简介
     * Spring3.1开始引入了的基于注释(annotation)的缓存(cache)技术，它本质上不是一个具体的缓存实现方案，而是一个对缓存使用的抽象，通过在既有代码中添加注解，即能够达到缓存方法的返回对象的效果。
     *
     * Spring 的缓存技术还具备相当的灵活性，不仅能够使用 SpEL 来定义缓存的 key 和各种 condition，还提供开箱即用的缓存临时存储方案，也支持和主流的专业缓存例如 Redis 集成。
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.NESTED)
    public User getUserById(String id) {
        User userById = userDao.getUserById(id);
//        Object o = AopContext.currentProxy();
//        int a = 1/0;
        return userById;
    }

    @Transactional
    @Cacheable(cacheNames = "users", key = "#id", sync = true)
    public User getUser(String id) {
        User userById = userDao.getUserById(id);
//        int a = 1/0;
        return userById;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        userDao.addUser(user);
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
//    @Transactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void updateById(User user) {
        userDao.updateById(user);

//        Object aopProxy = AopProxyContext.getAopProxy();
//        IUserService aopProxy1 = (IUserService) aopProxy;
//        aopProxy1.updateByName(user);
//        aopProxy1.updateByAge(user);
//        int a = 1/0;

    }

    /**
     * 验证spring事物在跨数据源下，是否能够正确切换数据源，答案是不能，spring的事物管理只支持单数据源，想要切数据源就只能
     * 进行分布式事物了
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    @DB(DB = DBTypeEnum.TEST2)
    public void updateByName(User user) {
        userDao.updateByName(user);
//        int a = 1/0;
    }

    /**
     * Propagation.MANDATORY,必须在一个事务中运行。也就是说，他只能被一个父事务调用。否则，他就要抛出异常
     * Propagation.NESTED,Nested的事务和他的父事务是相依的，他的提交是要等和他的父事务一块提交的。也就是说，如果父事务最后回滚，他也要回滚的。
     * Propagation.NEVER，以非事务方式执行，如果当前存在事务，则抛出异常。抛出异常后之前的事物会进行回滚
     * Propagation.NOT_SUPPORTED，以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。并获取一个新连接，如果是两次操作是更新同一个表，且某一个操作
     * 没有使用索引，则这个更新操作将升级为表锁，而由于之前的事物已经有锁存在，则会导致该操作锁超时，之前的事物会进行回滚操作
     * Propagation.REQUIRES_NEW，新建事务，如果当前存在事务，把当前事务挂起。如果是两次操作是更新同一个表，且某一个操作
     * 没有使用索引，则这个更新操作将升级为表锁，而由于之前的事物已经有锁存在，则会导致本次操作锁超时，两个事物会进行回滚操作
     * Propagation.SUPPORTS，当前如果有事物则加入到该事物，如果当前没有事务，就以非事务方式执行。也就是自动提交。
     *
     *      * @param user
     */
    @Transactional(rollbackFor = Exception.class,
            propagation = Propagation.MANDATORY)
//    @Transactional(rollbackFor = Exception.class,
//        propagation = Propagation.NESTED)
//    @Transactional(rollbackFor = Exception.class,
//            propagation = Propagation.NEVER)
//    @Transactional(rollbackFor = Exception.class,
//            propagation = Propagation.NOT_SUPPORTED)
//    @Transactional(rollbackFor = Exception.class,
//            propagation = Propagation.REQUIRES_NEW)
//    @Transactional(rollbackFor = Exception.class,
//            propagation = Propagation.SUPPORTS)
    public void updateByAge(User user) {
        userDao.updateByAge(user);

    }

}
