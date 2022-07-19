package com.daiyanping.cms.zookeeper.enjoy.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @Classname WatcherDemo
 * @Description TODO
 * @Author Jack
 * Date 2021/6/10 22:06
 * Version 1.0
 */
public class WatcherDemo implements Watcher {

    private CountDownLatch cdl;

    public WatcherDemo(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                cdl.countDown();
                System.out.println(event.getState());
                System.out.println("Zookeeper session established");
            }
            else if (Event.EventType.NodeCreated == event.getType()) {
                System.out.println("success create znode");

            }
            else if (Event.EventType.NodeDataChanged == event.getType()) {
                System.out.println("success change znode: " + event.getPath());

            }
            else if (Event.EventType.NodeDeleted == event.getType()) {
                System.out.println("success delete znode");

            }
            else if (Event.EventType.NodeChildrenChanged == event.getType()) {
                System.out.println("NodeChildrenChanged");

            }
        }
    }
}
