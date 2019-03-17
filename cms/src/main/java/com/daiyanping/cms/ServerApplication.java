package com.daiyanping.cms;

import com.daiyanping.cms.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	@Autowired
	private UserDao userDao;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}


