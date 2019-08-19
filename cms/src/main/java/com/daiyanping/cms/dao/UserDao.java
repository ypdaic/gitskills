package com.daiyanping.cms.dao;

import com.daiyanping.cms.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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


	List<Map<String, Object>> queryPage(@Param("id") Integer id);

	List<Map<String, Object>> queryPage(String id);

	@Insert("insert into user (name,password,age) values (#{user.name},#{user.password},#{user.age})")
	void addUser(@Param("user") User user);
}
