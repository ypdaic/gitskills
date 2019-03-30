package com.daiyanping.cms.dao;

import com.daiyanping.cms.entity.User;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
	
	List<User> getAllUser();
}
