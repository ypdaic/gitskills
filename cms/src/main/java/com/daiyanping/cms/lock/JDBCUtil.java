package com.daiyanping.cms.lock;

import com.alibaba.druid.pool.DruidDataSource;
import com.daiyanping.cms.dao.MsqlLockDao;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

public class JDBCUtil {

	public static DataSource getDataSource() {
//		SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
//		simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
//		simpleDriverDataSource.setPassword("test1234");
//		simpleDriverDataSource.setUsername("root");
//		simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
//		return simpleDriverDataSource;
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		druidDataSource.setUsername("root");
		druidDataSource.setPassword("test1234");
		druidDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC");
		druidDataSource.setMaxActive(10);
		return druidDataSource;
	}

	/**
	 * 基于mybatis获取SqlSession
	 * @return
	 */
	public static SqlSession getSqlSessionWithMybatis() {
		JdbcTransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("dev", jdbcTransactionFactory, getDataSource());
		Configuration configuration = new Configuration();
		configuration.setEnvironment(environment);
		configuration.setLogImpl(NoLoggingImpl.class);
		configuration.addMapper(MsqlLockDao.class);
		SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return sqlSession;
	}

	/**
	 * 基于mybatis-spring获取SqlSession
	 * @return
	 */
	public static SqlSession getSqlSessionWithMybatisSpring() {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(getDataSource());
		Configuration configuration = new Configuration();
		configuration.setLogImpl(NoLoggingImpl.class);
		configuration.addMapper(MsqlLockDao.class);
		sqlSessionFactoryBean.setConfiguration(configuration);
		SqlSession sqlSession = null;
		try {
			sqlSessionFactoryBean.afterPropertiesSet();
			SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
			sqlSession = new SqlSessionTemplate(sqlSessionFactory);
		} catch (Exception e) {
		}
		return sqlSession;
	}

	public static void colseSqlSessin(SqlSession sqlSession) {
		sqlSession.commit();
		sqlSession.close();
	}
}
