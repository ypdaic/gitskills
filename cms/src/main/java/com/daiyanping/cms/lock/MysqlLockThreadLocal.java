package com.daiyanping.cms.lock;

import org.apache.ibatis.session.SqlSession;

/**
 * @ClassName MysqlLockThreadLocal
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-12
 * @Version 0.1
 */
public class MysqlLockThreadLocal {

    public static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<>();

    public static SqlSession getSqlSession() {
        return threadLocal.get();
    }

    public static void setSqlSession(SqlSession sqlSession) {
        threadLocal.set(sqlSession);
    }

    public static void clean() {
        threadLocal.remove();
    }
}
