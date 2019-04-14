package com.daiyanping.cms.lock;

import com.daiyanping.cms.dao.MsqlLockDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @ClassName MysqlDistributedLockWithInster
 * @Description TODO 基于insert方式实现分布式锁
 * @Author daiyanping
 * @Date 2019-04-12
 * @Version 0.1
 */
public class MysqlDistributedLockWithInster extends AbstractDistributedLock implements Lock {

    private final SqlSession sqlSession = JDBCUtil.getSqlSessionWithMybatisSpring();

    @Override
    protected boolean getTryLock(String lock) {
        MsqlLockDao mapper = sqlSession.getMapper(MsqlLockDao.class);
        try {
            mapper.insertLock(lock);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void waitLock() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLock(String lock) {
        MsqlLockDao mapper = sqlSession.getMapper(MsqlLockDao.class);
        try {
            mapper.deleteLock(lock);
        } catch (Exception e) {

        } finally {

        }
    }
}
