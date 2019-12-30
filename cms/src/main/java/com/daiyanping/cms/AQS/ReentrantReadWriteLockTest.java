package com.daiyanping.cms.AQS;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ReentrantReadWriteLockTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-26
 * @Version 0.1
 */
public class ReentrantReadWriteLockTest {

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private static final Condition condition = readLock.newCondition();

    private static final Condition condition2 = writeLock.newCondition();

    public static void main(String[] args) {
//        test();
//        test2();
        test3();
    }

    /**
     * 验证写锁重入
     */
    public static void test() {
        writeLock.lock();
        try {
            writeLock.lock();
            try {

            } catch (Exception e) {

            } finally {
                writeLock.unlock();
            }
        } catch (Exception e) {

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 验证写锁降级为读锁
     * 结果：写锁可以降级为读锁
     */
    public static void test2() {
        writeLock.lock();
        System.out.println("加写锁");
        try {
            readLock.lock();
            System.out.println("加读锁");
            try {
                System.out.println("写锁降级为读锁");
            } catch (Exception e) {

            } finally {
                System.out.println("写锁降释放");
                writeLock.unlock();
            }
        } catch (Exception e) {

        } finally {
            System.out.println("读锁降释放");
            readLock.unlock();
        }
    }

    /**
     * 验证读锁升级为写锁
     * 结果:读锁不可以升级为写锁
     */
    public static void test3() {
        readLock.lock();
        System.out.println("加读锁");
        try {
            writeLock.lock();
            System.out.println("加写锁");
            try {
                System.out.println("写锁降级为读锁");
            } catch (Exception e) {

            } finally {
                System.out.println("读锁降释放");
                readLock.unlock();
            }
        } catch (Exception e) {

        } finally {
            System.out.println("写锁降释放");
            writeLock.unlock();
        }
    }
}
