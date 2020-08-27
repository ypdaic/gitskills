package com.daiyanping.cms.proxy;

import lombok.Data;

/**
 * @ClassName TestServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Data
public class TestServiceImpl implements ITestService {

    String s = "ss";

    private TestServiceImpl(String a) {

    }

    public TestServiceImpl() {

    }

    public TestServiceImpl(String a, String b) {
        this(a);
    }


    @Override
    public void say() {
        System.out.println("aaa");
    }


    public synchronized void say2() {
        try {
            Thread.sleep(1000 *10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("aaa");
    }
}
