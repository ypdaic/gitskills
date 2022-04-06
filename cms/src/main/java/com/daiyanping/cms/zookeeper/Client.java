package com.daiyanping.cms.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * Client
 *
 * @author daiyanping
 * @date 2022-03-26
 * @description
 */
public class Client implements Watcher {

    ZooKeeper zooKeeper;

    String hostPort;

    CountDownLatch countDownLatch;

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public Client(String hostPort) {
        this.hostPort = hostPort;
        countDownLatch = new CountDownLatch(1);
    }

    public void startZK() throws IOException {
        zooKeeper = new ZooKeeper(hostPort, 15000, this);
    }

    public void stopZK() throws InterruptedException {
        zooKeeper.close();
    }

    public String queueCommand(String command) throws Exception {
        while (true) {
            String name = "";
            try {
                name = zooKeeper.create("/tasks/task-", command.getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
                return name;
            } catch (KeeperException e) {
                if (e instanceof KeeperException.NodeExistsException) {
                    throw new Exception(name + " already appears to be running");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("k8s-node1:2181");
        client.startZK();
        String name = client.queueCommand("test");
        System.out.println("Created "+ name);
    }
}
