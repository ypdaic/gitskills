package com.daiyanping.cms.lock;

import com.daiyanping.cms.dao.MsqlLockDao;
import org.apache.ibatis.session.SqlSession;

/**
 * 基于update方式实现mysql分布式锁
 */
public class MysqlDistributedLockWithUptate extends AbstractDistributedLock implements Lock {

	@Override
	protected boolean getTryLock(String lock) {
		SqlSession sqlSession = MysqlLockThreadLocal.getSqlSession();
		if (sqlSession == null) {
			sqlSession = JDBCUtil.getSqlSessionWithMybatis();
			MysqlLockThreadLocal.setSqlSession(sqlSession);
		}
		MsqlLockDao mapper = sqlSession.getMapper(MsqlLockDao.class);
		try {
			mapper.update(lock);
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
		JDBCUtil.colseSqlSessin(sqlSession);
		MysqlLockThreadLocal.clean();
	}
}
