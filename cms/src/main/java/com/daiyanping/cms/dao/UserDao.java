package com.daiyanping.cms.dao;

import com.daiyanping.cms.entity.User;
import org.springframework.context.annotation.Import;

import java.util.List;


public interface UserDao {
	
	List<User> getAllUser();
}
