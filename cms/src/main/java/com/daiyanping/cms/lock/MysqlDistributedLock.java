package com.daiyanping.cms.lock;

import com.daiyanping.cms.dao.MsqlLockDao;
import org.apache.ibatis.session.SqlSession;

public class MysqlDistributedLock extends AbstractDistributedLock implements Lock {

	public static SqlSession sqlSession = JDBCUtil.getSqlsession();;
	public static  MsqlLockDao mapper = sqlSession.getMapper(MsqlLockDao.class);
	@Override
	protected boolean getTryLock(String lock) {
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
		sqlSession.commit();
		sqlSession.close();
	}
}
