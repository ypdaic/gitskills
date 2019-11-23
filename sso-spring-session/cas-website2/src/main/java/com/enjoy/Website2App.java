package com.enjoy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class Website2App {

	public static void main(String[] args) {
		SpringApplication.run(Website2App.class, args);
	}
}
