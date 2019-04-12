package com.daiyanping.cms.lock;

import com.daiyanping.cms.dao.MsqlLockDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @ClassName MysqlDistributedLockWithInster
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-12
 * @Version 0.1
 */
public class MysqlDistributedLockWithInster extends AbstractDistributedLock implements Lock {

    @Override
    protected boolean getTryLock(String lock) {
        SqlSession sqlSession = MysqlLockThreadLocal.getSqlSession();
        if (sqlSession == null) {
            sqlSession = JDBCUtil.getSqlSessionWithMybatis();
            MysqlLockThreadLocal.setSqlSession(sqlSession);
        }
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
        SqlSession sqlSession = MysqlLockThreadLocal.getSqlSession();
        MsqlLockDao mapper = sqlSession.getMapper(MsqlLockDao.class);
        try {
            mapper.deleteLock(lock);
        } catch (Exception e) {

        } finally {
            JDBCUtil.colseSqlSessin(sqlSession);
            MysqlLockThreadLocal.clean();
        }
    }
}
