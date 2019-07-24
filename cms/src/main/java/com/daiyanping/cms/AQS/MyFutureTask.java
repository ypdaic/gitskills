package com.daiyanping.cms.AQS;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName MyFutureTask
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-24
 * @Version 0.1
 */
public class MyFutureTask<V> implements RunnableFuture<V> {

    private Callable<V> callable;

    private volatile V v;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private volatile boolean cancelState = false;


    public MyFutureTask(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        lock.lock();
        try {

            if (!cancelState) {

                try {
                    v = callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning == true) {
            Thread.currentThread().interrupt();
        }
        cancelState = true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return cancelState;
    }

    @Override
    public boolean isDone() {
        return v == null ? false : true;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        lock.lock();
        try {

            if (v == null) {
                condition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return v;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean b = lock.tryLock(timeout, unit);
        if (b) {
            try {

                if (v == null) {
                    boolean await = condition.await(timeout, unit);
                    if (!await) {
                        throw new TimeoutException();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else {
            throw new TimeoutException();
        }
        return v;
    }
}
