package com.daiyanping.cms.javabase;

import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName LockSupportTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-25
 * @Version 0.1
 */
public class LockSupportTest {

    /**
     * 验证执行park方法时，线程是处于WAITING状态
     *
     * 名称: LockSupportTest
     * 状态: WAITING
     * 总阻止数: 0, 总等待数: 1
     *
     * 堆栈跟踪:
     * sun.misc.Unsafe.park(Native Method)
     * java.util.concurrent.locks.LockSupport.park(LockSupport.java:304)
     * com.daiyanping.cms.javabase.LockSupportTest.main(LockSupportTest.java:20)
     * @param args
     */
    public static void main(String[] args) {
        Thread.currentThread().setName("LockSupportTest");
        LockSupport.park();
    }
}
