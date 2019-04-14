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

    private final static LockThreadLocal<ZkClient> lockThreadLocal = new LockThreadLocal<>();

    private final static LockThreadLocal<CountDownLatch> lockThreadLocal2 = new LockThreadLocal<>();

    private final static String LOCK_PATH = "/lock";

    @Override
    protected boolean getTryLock(String lock) {
        ZkClient zkClient = lockThreadLocal.getT();
        if (zkClient == null) {
            zkClient = new ZkClient("127.0.0.1:2181");
            lockThreadLocal.setT(zkClient);
        }
        CountDownLatch countDownLatch = lockThreadLocal2.getT();
        if (countDownLatch == null) {
            countDownLatch = new CountDownLatch(1);
            lockThreadLocal2.setT(countDownLatch);
        }
        zkClient.subscribeChildChanges(LOCK_PATH, new IZkChildListener() {

            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("收到节点变化事件");
                CountDownLatch countDownLatch = lockThreadLocal2.getT();

                countDownLatch.countDown();
                lockThreadLocal2.clean();
            }

        });
        try {
            zkClient.createEphemeral(LOCK_PATH + lock);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void waitLock() {
        CountDownLatch countDownLatch = lockThreadLocal2.getT();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLock(String lock) {
        ZkClient zkClient = lockThreadLocal.getT();
        zkClient.delete(lock);
        zkClient.close();
        lockThreadLocal.clean();

    }

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        zkClient.subscribeChildChanges("/test", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath);
                System.out.println(currentChilds);
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
