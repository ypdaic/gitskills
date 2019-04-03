package com.daiyanping.cms.dao;

import com.daiyanping.cms.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

	@Select("select * from user")
	List<User> getAllUser();
}
