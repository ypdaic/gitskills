package com.daiyanping.cms.spring.bean;

public class FactoryBean {

    public Object factoryMethod() {
        System.out.println("=========factoryMethod=========");
        return new PropertyClass();
    }
}
