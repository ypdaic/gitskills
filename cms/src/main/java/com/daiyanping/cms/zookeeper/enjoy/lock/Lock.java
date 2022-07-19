package com.daiyanping.cms.zookeeper.enjoy.lock;

/**
 * @Classname Lock
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 20:23
 * Version 1.0
 */
public interface Lock {
    public void lock();
    public void unlock();
}
