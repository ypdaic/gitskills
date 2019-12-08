package com.daiyanping.cms.mongodb.nativeapi.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="fam")
public class Doc {
	
	private String id;
	
	private String name;
	
	private int age;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Doc [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	
}