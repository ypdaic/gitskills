package com.daiyanping.cms.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

import static org.apache.zookeeper.CreateMode.EPHEMERAL;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * Master 实现同步master 选举
 *
 * @author daiyanping
 * @date 2022-03-26
 * @description
 */
public class Master implements Watcher {

    ZooKeeper zooKeeper;

    String hostPort;

    String serverId;

    boolean isLeader;

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    // 同步选主
    public void runForMaster() throws InterruptedException {
        while (true) {
            try {
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
                zooKeeper.create("/master", serverId.getBytes(), OPEN_ACL_UNSAFE, EPHEMERAL);
                isLeader = true;
                break;
            } catch (KeeperException e) {
                if (e instanceof KeeperException.NodeExistsException) {
                    isLeader = false;
                    break;
                }
            }
            if (checkMaster()) {
                break;
            }
        }

    }

    // return ture is there is a master
    public boolean checkMaster() throws InterruptedException {
        while (true) {
            try {
                Stat stat = new Stat();

                byte[] data = zooKeeper.getData("/master", false, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (KeeperException e) {
                if (e instanceof KeeperException.NoNodeException) {

                    // no master,so try create again
                    return false;
                }
            }
        }
    }

    public void startZK() throws IOException {
        zooKeeper = new ZooKeeper(hostPort, 15000, this);
    }

    public void stopZK() throws InterruptedException {
        zooKeeper.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Master master = new Master("k8s-node1:2181");
        master.startZK();
        master.runForMaster();
        if (master.isLeader) {
            System.out.println("Im the leader");
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is the leader");
        }
        master.stopZK();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }
}
