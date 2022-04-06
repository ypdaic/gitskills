package com.daiyanping.cms.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * AdminClient
 *
 * @author daiyanping
 * @date 2022-03-26
 * @description
 */
public class AdminClient implements Watcher {

    ZooKeeper zooKeeper;

    String hostPort;

    CountDownLatch countDownLatch;

    public AdminClient(String hostPort) {
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

    public void listState() throws InterruptedException, KeeperException {
        try {

            Stat stat = new Stat();
            byte[] masterData = zooKeeper.getData("/master", false, stat);
            Date startDate = new Date(stat.getCtime());
            System.out.println("Master:" + new String(masterData) + " since " + startDate);

        } catch (KeeperException e) {
            if (e instanceof KeeperException.NoNodeException) {
                System.out.println("No Master");
            }
        }

        System.out.println("Workers:");
        List<String> children = zooKeeper.getChildren("/workers", false);
        for (String child : children) {
            byte[] data = zooKeeper.getData("/workers/" + child, false, null);
            String state = new String(data);
            System.out.println("\t" + child + ": " + state);
        }

        System.out.println("Tasks:");
        List<String> children1 = zooKeeper.getChildren("/assign", false);
        for (String s : children1) {
            System.out.println("\t + t");
        }

    }

    public static void main(String[] args) throws Exception {
        AdminClient adminClient = new AdminClient("k8s-node1:2181");
        adminClient.startZK();
        adminClient.listState();
    }
}
