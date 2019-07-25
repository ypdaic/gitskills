package com.daiyanping.cms.AQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName MyLock
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-25
 * @Version 0.1
 */
public class MyLock implements Lock {

    private Syn syn;

    public MyLock() {
        syn = new Syn();
    }

    @Override
    public void lock() {
        syn.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        syn.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return syn.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return syn.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        syn.release(1);
    }

    @Override
    public Condition newCondition() {
        return syn.new ConditionObject();
    }

    private static class Syn extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            int state = getState();
            if (state == 0 && compareAndSetState(state, arg)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            } else if (getExclusiveOwnerThread() == Thread.currentThread()){
                // 支持可重入
                int state1 = getState();
                setState(state1 + arg);
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (!isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }

            int state = getState();
            int newValue = state - arg;
            if (compareAndSetState(state, newValue)) {
                if (newValue == 0) {

                    setExclusiveOwnerThread(null);
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected boolean isHeldExclusively() {
            Thread exclusiveOwnerThread = getExclusiveOwnerThread();
            if (Thread.currentThread() == exclusiveOwnerThread) {
                return true;
            } else {
                return false;
            }
        }
    }
}
