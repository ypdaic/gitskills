package com.daiyanping.cms.lock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName ZookeeperDistributedLock
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-12
 * @Version 0.1
 */
public class ZookeeperDistributedLock extends AbstractDistributedLock implements Lock {

    private final ZkClient zkClient = new ZkClient("127.0.0.1:2181");

    @Override
    protected boolean getTryLock(String lock) {
        try {

            zkClient.createEphemeral(lock);
        } catch (Exception e) {

            return false;
        }

        return true;
    }

    @Override
    protected void waitLock() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        zkClient.subscribeChildChanges("/", new IZkChildListener() {

            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("收到节点变化事件");
                countDownLatch.countDown();
            }

        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLock(String lock) {
        zkClient.delete(lock);
    }
}
