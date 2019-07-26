package com.daiyanping.cms.AQS;

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

    public static void main(String[] args) {
        test();
    }

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
}
