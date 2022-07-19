package com.daiyanping.cms.zookeeper.enjoy.lock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Classname RWTest
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 22:18
 * Version 1.0
 */
public class RWTest {

    private String root = "/RWLock";

    @Test
    public void test1() throws InterruptedException {
        RWLock lock = new RWLock();
        lock.initClient("192.168.67.139:2184", root);
        lock.rLock();
        //下面就进行读业务
        System.out.println("test1--RLOCK");
        System.out.println("======休眠30s,30s后释放锁=========");
        for (int i = 0; i < 30; i++) {
            System.out.println(30 - i);
            TimeUnit.SECONDS.sleep(1);
        }
        lock.unRLock();
    }

    @Test
    public void test2() throws InterruptedException {
        RWLock lock = new RWLock();
        lock.initClient("192.168.67.139:2184", root);
        lock.rLock();
        //下面就进行读业务
        System.out.println("test1--RLOCK");
        System.out.println("======休眠20s,20s后释放锁=========");
        for (int i = 0; i < 10; i++) {
            System.out.println(10 - i);
            TimeUnit.SECONDS.sleep(1);
        }
        lock.unRLock();
    }

    @Test
    public void test3() throws InterruptedException {
        RWLock lock = new RWLock();
        lock.initClient("192.168.67.139:2184", root);
        lock.wLock();
        //下面就进行读业务
        System.out.println("test3--WLOCK");
        System.out.println("======休眠20s,20s后释放锁=========");
        for (int i = 0; i < 20; i++) {
            System.out.println(20 - i);
            TimeUnit.SECONDS.sleep(1);
        }
        lock.unWLock();
    }

    @Test
    public void test4() throws InterruptedException {
        RWLock lock = new RWLock();
        lock.initClient("192.168.67.139:2184", root);
        lock.rLock();
        //下面就进行读业务
        System.out.println("test4--RLOCK");
        lock.unRLock();
    }
}
