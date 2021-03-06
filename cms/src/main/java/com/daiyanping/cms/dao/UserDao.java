package com.daiyanping.cms.dao;

import com.daiyanping.cms.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

	@Select("select * from user where id=#{id}")
    User getUserById(@Param("id") String id);

	List<Map<String, Object>> queryPage();

	List<Map<String, Object>> queryPage(String id);
}
