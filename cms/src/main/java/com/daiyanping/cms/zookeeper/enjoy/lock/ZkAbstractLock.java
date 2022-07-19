package com.daiyanping.cms.zookeeper.enjoy.lock;

import org.I0Itec.zkclient.ZkClient;

/**
 * @Classname ZkAbstractLock
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 20:24
 * Version 1.0
 */
public abstract class ZkAbstractLock implements Lock {

    private static String connectStr = "192.168.67.139:2184";

    public static String path = "/lock";

    protected ZkClient client = new ZkClient(connectStr);

    /**
        lock方式是要去获取锁的方法
        如果成功，那么代码往下走，执行创建订单的业务逻辑

        如果失败,lock需要等待
        1、等待到了前面那个获取锁的客户端释放锁以后
        2、再去重新获取锁
    */
    @Override
    public void lock() {
        //1、尝试去获取锁
        if(tryLock()) {
            System.out.println(Thread.currentThread().getName() + "--->获取锁成功！");
        } else {
            //在这里等待
            waitforlock();
            lock();
        }
    }

    protected abstract boolean tryLock();

    protected abstract void waitforlock();

    @Override
    public void unlock() {
        client.close();
    }
}
