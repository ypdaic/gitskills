package com.daiyanping.cms.zookeeper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.CreateMode.EPHEMERAL;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * AsyncMaster 实现异步master 选举
 *
 * @author daiyanping
 * @date 2022-03-26
 * @description
 */
@Slf4j
public class AsyncMaster implements Watcher {

    ZooKeeper zooKeeper;

    String hostPort;

    String serverId;

    boolean isLeader;

    CountDownLatch countDownLatch;

    public AsyncMaster(String hostPort) {
        this.hostPort = hostPort;
        countDownLatch = new CountDownLatch(1);
    }

    // 同步选主
    public void runForMaster() throws InterruptedException {
        Random random = new Random();
        serverId = Integer.toHexString(random.nextInt());
        /**
         * create 方法会抛出两种异常，KeeperException, InterruptedException，我们需要处理这两种异常，特别是
         * ConnectionLossException和InterruptedException,对于其他异常，我们可以忽略并继续执行，但对于这两种异常，create方法
         * 可能已经成功了，所以如果我们作为主节点就需要捕获并处理它们
         * ConnectionLossException异常发生于客户端与Zookeeper服务端失去连接时，一般常常由于网络原因导致，如网络分区或Zookeeper
         * 服务器故障，当这个异常发生时，客户端并不知道是在Zookeeper服务器处理前丢失了请求消息，还是在处理后客户端未收到响应消息
         * 如我们之前所描述的，Zookeeper的客户端将会为后续请求重新建立连接，但进程必须知道一个未决请求是否已经处理了还是需要再次
         * 发送请求
         * InterruptedException异常常源于客户端线程调用了Thread.interrupt,通常这是因为应用程序部分关闭，但还在被其他相关应用的
         * 方法使用，从字面来看这个异常，进程会中段本地客户端的请求处理的过程，并使该请求处于未知状态
         * 这两种请求都会导致正常请求处理过程的中断，开发者不能假设处理过程中的请求的状态，当我们处理这些异常时，开发者在处理前必须
         * 知道系统的状态，如果发生群首选举，在我们没有确认情况之前，我们不希望确定主节点，如果create执行成功了，活动主节点死掉以前
         * 没有任何进程能够成为主节点
         */
        zooKeeper.create("/master", serverId.getBytes(), OPEN_ACL_UNSAFE, EPHEMERAL, new MasterCreateCallBack(), null);

    }

    public void checkMaster() throws InterruptedException {
        zooKeeper.getData("/master", false, new MasterCheckCallback(), null);
    }

    public void startZK() throws IOException {
        zooKeeper = new ZooKeeper(hostPort, 15000, this);
    }

    public void stopZK() throws InterruptedException {
        zooKeeper.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AsyncMaster master = new AsyncMaster("k8s-node1:2181");
        master.startZK();
        master.runForMaster();
        master.countDownLatch.await();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public class MasterCreateCallBack implements AsyncCallback.StringCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    try {
                        checkMaster();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                case OK:
                    isLeader = true;
                    break;
                default:
                    isLeader = false;
            }

            System.out.println("Im " + (isLeader ? "" : "not " ) + "the leader");
        }
    }

    public class MasterCheckCallback implements AsyncCallback.DataCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    try {
                        checkMaster();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                case NONODE:
                    try {
                        runForMaster();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
            }
        }
    }

    public class CreateParentCallback implements AsyncCallback.StringCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createParent(path, (byte[]) ctx);
                    break;
                case OK:
                    log.info("Parent created");
                    break;
                case NODEEXISTS:
                    log.warn("Parent already registered: " + path);
                    break;
                default:
                    log.error("Something went wrong: ", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    }

    public void createParent(String path, byte[] data) {
        zooKeeper.create(path, data, OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new CreateParentCallback(), data);
    }

    public void bootstrap() {
        createParent("/workers", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
    }

}
