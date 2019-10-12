package com.daiyanping.cms.proxy;

/**
 * @ClassName TestServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
public class TestServiceImpl implements ITestService {

    private TestServiceImpl(String a) {

    }

    public TestServiceImpl(String a, String b) {
        this(a);
    }


    @Override
    public void say() {
        System.out.println("aaa");
    }


    public synchronized void say2() {
        System.out.println("aaa");
    }
}
