package com.daiyanping.cms.javabase.extendss;

public class Test {

    public static void main(String[] args) {

        Student mStudent = new Student("abc");

        String mName = mStudent.getName();


        System.out.println("Name is : " + mName);


        mStudent.setName("efg");

        mName = mStudent.getName();

        System.out.println("Name is : " + mName);

    }
}
