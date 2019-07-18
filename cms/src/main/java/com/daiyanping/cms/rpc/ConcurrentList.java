package com.daiyanping.cms.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConcurrentList
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-18
 * @Version 0.1
 */
public class ConcurrentList<T> {

    private ReentrantLock lock = new ReentrantLock();
    private volatile List<T> list = new ArrayList<T>();

    public List<T> get() {
        ArrayList<T> result = new ArrayList<T>();
        lock.lock();
        list.forEach(t -> {
            result.add(t);
        });
        lock.unlock();
        return result;

    }

    public void set(T t){
        lock.lock();
        list.add(t);
        lock.unlock();
    }

}
