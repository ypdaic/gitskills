package com.daiyanping.cms.javabase.classload;

public class ClassInit2Test {

    private int num = 1;


    /**
     * 此时不存在<client>方法，但是存在<init>方法，也就是默认的构造器方法
     * @param args
     */
    public static void main(String[] args) {
        int b = 2;
    }
}
