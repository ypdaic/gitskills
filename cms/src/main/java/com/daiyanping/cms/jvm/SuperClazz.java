package com.daiyanping.cms.jvm;

/**
 * @ClassName SuperClazz
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/7/31
 * @Version 0.1
 */
public class SuperClazz {
    static {
        System.out.println("SuperClass init!");

    }

    public static int value = 123;

    public static final String HELLOWORLD = "hello test";

    public static final int WHAT = value;
}
