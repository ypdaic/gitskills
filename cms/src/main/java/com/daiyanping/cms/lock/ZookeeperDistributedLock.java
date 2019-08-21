package com.daiyanping.cms.lock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ZookeeperDistributedLock
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-12
 * @Version 0.1
 */
public class ZookeeperDistributedLock extends AbstractDistributedLock implements Lock {

    private final static String LOCK_PATH = "/lock";

    private static ThreadLocal<ZkClient> zkClientThreadLocal = new ThreadLocal<>();

    @Override
    protected boolean getTryLock(String lock) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 3000);
        try {
            zkClient.createPersistent(LOCK_PATH + lock, true);
        } catch (Exception e) {
            return false;
        } finally {
            zkClientThreadLocal.set(zkClient);
        }
        return true;
    }

    @Override
    protected void waitLock() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IZkChildListener iZkChildListener = new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                countDownLatch.countDown();
            }
        };

        ZkClient zkClient = zkClientThreadLocal.get();
        zkClient.subscribeChildChanges(LOCK_PATH, iZkChildListener);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unLock(String lock) {
        ZkClient zkClient = zkClientThreadLocal.get();
        try {

            zkClient.delete(LOCK_PATH + lock);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkClient.close();
            zkClientThreadLocal.remove();
        }
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
