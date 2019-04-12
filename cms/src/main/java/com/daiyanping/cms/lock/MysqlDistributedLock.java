package com.daiyanping.cms.lock;

import com.daiyanping.cms.dao.MsqlLockDao;
import org.apache.ibatis.session.SqlSession;

public class MysqlDistributedLock extends AbstractDistributedLock implements Lock {

	@Override
	protected boolean getTryLock(String lock) {
		SqlSession sqlSession = MysqlLockThreadLocal.getSqlSession();
		if (sqlSession == null) {
			sqlSession = JDBCUtil.getSqlSession();
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
	public void unLock() {
		SqlSession sqlSession = MysqlLockThreadLocal.getSqlSession();
		JDBCUtil.colseSqlSessin(sqlSession);
		MysqlLockThreadLocal.clean();
	}
}
