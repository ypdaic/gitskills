package com.daiyanping.cms.spring.bean;

public class FactoryBean {

    public Object factoryMethod(/*String id,List param*/) {
        System.out.println("=========factoryMethod=========");
        return new PropertyClass();
    }
}
