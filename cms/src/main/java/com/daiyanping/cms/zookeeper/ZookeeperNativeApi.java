package com.daiyanping.cms.zookeeper;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.CreateMode.EPHEMERAL;
import static org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

/**
 * @ClassName ZookeeperNativeApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-20
 * @Version 0.1
 */
@Data
public class ZookeeperNativeApi implements Watcher {

    private static String SERVER_IP_PORT = "192.168.0.4:2181";

    CountDownLatch countDownLatch = new CountDownLatch(1);

    ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1 () throws Exception {
        ZookeeperNativeApi zookeeperNativeApi = new ZookeeperNativeApi();

        ZooKeeper zooKeeper = new ZooKeeper(SERVER_IP_PORT, 30000000, zookeeperNativeApi);
        zookeeperNativeApi.countDownLatch.await();
        zookeeperNativeApi.setZooKeeper(zooKeeper);

        // 需要事先添加exits事件，否则不能监听到节点创建事件
        zooKeeper.exists("/test", true);
        // 创建一个临时anyone权限的节点(临时节点不能创建子节点)
        String s = zooKeeper.create("/test", "haha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, EPHEMERAL);
        System.out.println(s);

        // 获取节点数据
        byte[] data = zooKeeper.getData("/test", true, null);
        System.out.println(new String(data));

        // 变更节点数据，版本给-1表示使用最新的数据进行变更
        zooKeeper.setData("/test", "改变节点数据".getBytes(), -1);

        // 变更节点数据，版本给-1表示使用最新的数据进行变更
        zooKeeper.setData("/test", "改变节点数据2".getBytes(), -1);


        // 如果需要创建名称相同的节点，节点类型需要是有序的,注意有序节点没法监控
        String nodePath = zooKeeper.create("/test", "hahadd".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, EPHEMERAL_SEQUENTIAL);
        System.out.println(nodePath);
        byte[] data1 = zooKeeper.getData(nodePath, null, null);
        System.out.println(new String(data1));

        // 删除节点
        zooKeeper.delete(nodePath, -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                // 成功 rc则是0，错误则是相应的错误码
                System.out.println(rc);
                System.out.println(path);
                System.out.println(ctx);
            }
        }, "自己的上下文");

        // 判断节点是否存在
        zooKeeper.exists("/test", zookeeperNativeApi, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                System.out.println(rc);
                System.out.println(path);
                System.out.println(ctx);
                System.out.println(stat);
            }
        }, "自己的上下文");

        zooKeeper.close();
    }

    @Override
    public void process(WatchedEvent event) {
        try {

            Event.KeeperState state = event.getState();
            Event.EventType type = event.getType();
            if (state.getIntValue() == Event.KeeperState.SyncConnected.getIntValue()) {

                if (type.getIntValue() == Event.EventType.None.getIntValue()) {
                    this.countDownLatch.countDown();
                    System.out.println("连接事件");
                }

                if (type.getIntValue() == Event.EventType.NodeCreated.getIntValue()) {
                    String path = event.getPath();
                    System.out.println(path + ":创建节点事件");
                    // 再次添加exits事件，否则不能监听到节点创建事件
                    zooKeeper.exists(path, true);
            }

                if (type.getIntValue() == Event.EventType.NodeDataChanged.getIntValue()) {
                    String path = event.getPath();
                    System.out.println(path + ":节点数据变更事件");
                    // 由于watch机制是一次性的，在搜到节点变更事件后，需要重新注册数据变更事件
                    byte[] data = zooKeeper.getData(path, true, null);
                    System.out.println("变更后的数据:" + new String(data));
                }

                if (type.getIntValue() == Event.EventType.NodeChildrenChanged.getIntValue()) {
                    String path = event.getPath();
                    System.out.println(path + ":子节点变更事件");
                    zooKeeper.getChildren(path, true);
                }

                if (type.getIntValue() == Event.EventType.NodeDeleted.getIntValue()) {
                    String path = event.getPath();
                    System.out.println("节点删除事件");
                    // 再次添加exits事件，否则不能监听到节点创建事件
                    zooKeeper.exists(path, true);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
