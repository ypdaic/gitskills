package com.daiyanping.cms.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Worker
 *
 * @author daiyanping
 * @date 2022-03-26
 * @description
 */
@Slf4j
public class Worker implements Watcher {

    ZooKeeper zooKeeper;

    String hostPort;

    String serverId;

    String status;

    CountDownLatch countDownLatch;

    public Worker(String hostPort) {
        this.hostPort = hostPort;
        countDownLatch = new CountDownLatch(1);
    }

    public void startZK() throws IOException {
        zooKeeper = new ZooKeeper(hostPort, 15000, this);
    }

    public void stopZK() throws InterruptedException {
        zooKeeper.close();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public void register() {
        Random random = new Random();
        serverId = Integer.toHexString(random.nextInt());
        zooKeeper.create("/workers/worker-" + serverId, "Idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new CreateWorkerCallback(), null);
    }

    public class CreateWorkerCallback implements AsyncCallback.StringCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    log.info("Registered successfully: " + serverId);
                    break;
                case NODEEXISTS:
                    log.warn("Already registered: " + serverId);
                    break;
                default:
                    log.error("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc
                    ), path));
            }
        }
    }

    /**
     * 顺序和ConnectionLossException
     * Zookeeper会严格维护执行顺序，并提供强有力的顺序保障，然而，在多线程下还是需要小心面对顺序问题，多线程下，当
     * 回调函数中包括重试逻辑的代码时，一些常见的场景都可能导致错误发生，当遇到ConnectionLossException异常而补发
     * 一个请求时，新建立的请求可能排序在其他线程中的请求之后，而实际上其他线程中的请求应该在原来请求之后
     */
    public class StatusUpdateCallback implements AsyncCallback.StatCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    updateStatus((String) ctx);
                    return;
            }
        }


    }

    /**
     * 加这个判断是因为有可能在上一次设置状态时，出现了网络异常，然后再次更新状态后，上一次的回调过来了，判断丢失连接后又将状态设置为旧
     * 的状态了，只有和当前worker本地的状态一致时才可以更新zk上的状态
     * @param status
     */
    synchronized private void updateStatus(String status) {
        if (this.equals(status)) {
            // -1 表示禁止版本号检查
            zooKeeper.setData("/workers/worker-" + serverId, status.getBytes(), -1, new StatusUpdateCallback(), status);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        updateStatus(status);
    }

    public static void main(String[] args) throws Exception {
        Worker worker = new Worker("k8s-node1:2181");
        worker.startZK();
        worker.register();
        worker.countDownLatch.await();
    }
}

