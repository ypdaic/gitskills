package com.daiyanping.cms.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable{
	
	private String name;
	
	private Integer id;
	
	private String password;
	
	private Integer age;
	
	
}
