package com.daiyanping.cms.zookeeper.enjoy.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.exception.ZkException;

import java.util.concurrent.CountDownLatch;

/**
 * @Classname ZkLockImpl
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 20:33
 * Version 1.0
 */
public class ZkLockImpl extends ZkAbstractLock {

    private CountDownLatch cdl = null;

    //尝试获取锁
    @Override
    protected boolean tryLock() {
        try {
            client.createEphemeral(path);
            return true;
        } catch (ZkException e) {
            return false;
        }
    }

    //等待获取锁
    //等前面那个获取锁成功的客户端释放锁

    //没有获取到锁的客户端都会走到这里
    //1、没有获取到锁的要注册对/lock节点的watcher
    //2、这个方法需要等待
    @Override
    protected void waitforlock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
            //一旦/lock节点被删除以后，就会触发这个方法
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                //让等待的代码不再等待了
                if(cdl != null) {
                    cdl.countDown();
                }
            }
        };
        //注册watcher
        client.subscribeDataChanges(path, iZkDataListener);

        if (client.exists(path)) {
            cdl = new CountDownLatch(1);
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //取消该客户端的订阅关系
        client.unsubscribeDataChanges(path, iZkDataListener);
    }
}
