package com.daiyanping.cms.dao;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MsqlLockDao {

	@Update("update mysqllock set lock_name = #{lock} where id = 1")
	void update(@Param("lock") String lock);
}
