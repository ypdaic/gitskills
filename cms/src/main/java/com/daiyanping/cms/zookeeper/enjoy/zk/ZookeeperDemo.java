package com.daiyanping.cms.zookeeper.enjoy.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import sun.security.acl.AclEntryImpl;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Classname ZookeeperDemo
 * @Description TODO
 * @Author Jack
 * Date 2021/6/10 22:04
 * Version 1.0
 */
public class ZookeeperDemo {

    private static String connectStr = "192.168.67.139:2184";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        //建立连接本身就是一个异步过程
        //埋了一个代码
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, 5000, new WatcherDemo(countDownLatch));
        countDownLatch.await();

//        nodeCreate(zooKeeper,"/demo1");
//        nodeChange(zooKeeper,"/demo1","Jack2");
        nodeDelete(zooKeeper,"/demo1");
    }

    private static void nodeDelete(ZooKeeper client,String path) {
        try {
            client.exists(path,true);
            client.delete(path,-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void nodeChange(ZooKeeper client,String path,String value) {
        try {
            client.exists(path,true);
            client.setData(path,value.getBytes(),-1);

            client.getData(path,true,new Stat());
            client.setData(path,value.getBytes(),-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void nodeCreate(ZooKeeper client,String path) {
        try {
            client.create(path,"jack".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
