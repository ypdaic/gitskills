package com.daiyanping.cms.lock;

/**
 * @ClassName ZookeeperDistributedLock
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-12
 * @Version 0.1
 */
public class ZookeeperDistributedLock extends AbstractDistributedLock implements Lock {
    @Override
    protected boolean getTryLock(String lock) {
        return false;
    }

    @Override
    protected void waitLock() {

    }

    @Override
    public void unLock(String lock) {

    }
}
