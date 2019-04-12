package com.daiyanping.cms.dao;


import org.apache.ibatis.annotations.*;

public interface MsqlLockDao {

	@Update("update mysql_lock set lock_name = #{lock} where id = 1")
	void update(@Param("lock") String lock);

	@Insert("insert into mysql_lock (lock_name) values (#{lock})")
	void insertLock(@Param("lock") String lock);

	@Delete("delete from mysql_lock where lock_name = #{lock}")
	void deleteLock(@Param("lock") String lock);
}
