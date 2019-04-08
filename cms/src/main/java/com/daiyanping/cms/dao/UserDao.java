package com.daiyanping.cms.dao;

import com.daiyanping.cms.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

	@Select("select * from user")
	List<User> getAllUser();

	@Update("update user set name = #{dto.name} where id = #{dto.id}")
	void updateById(@Param("dto") User user);

	@Update("update user set age = #{dto.age} where name = #{dto.name}")
	void updateByName(@Param("dto") User user);

	@Update("update user set name = #{dto.name} where  id= 2")
    void updateByAge(@Param("dto") User user);
}
