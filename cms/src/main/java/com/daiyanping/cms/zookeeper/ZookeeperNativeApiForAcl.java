package com.daiyanping.cms.zookeeper;

import lombok.Data;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.CreateMode.EPHEMERAL;

/**
 * @ClassName ZookeeperNativeApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-20
 * @Version 0.1
 */
@Data
public class ZookeeperNativeApiForAcl implements Watcher {

    private static String SERVER_IP_PORT = "192.168.0.4:2181";

    CountDownLatch countDownLatch = new CountDownLatch(1);

    ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1 () throws Exception {
        ZookeeperNativeApiForAcl zookeeperNativeApi = new ZookeeperNativeApiForAcl();

        ZooKeeper zooKeeper = new ZooKeeper(SERVER_IP_PORT, 3000, zookeeperNativeApi);

        // 添加用户
        zooKeeper.addAuthInfo("digest", "123456".getBytes());
        zookeeperNativeApi.countDownLatch.await();
        zookeeperNativeApi.setZooKeeper(zooKeeper);

        // 使用digest的话，就需要使用加密后的密码
//        String s1 = DigestAuthenticationProvider.generateDigest("123456");
//        List<ACL> digest = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, new Id("digest", s1)));

        List<ACL> digest = Collections.singletonList(new ACL(ZooDefs.Perms.ALL , new Id("auth", "1234567")));

        // 需要事先添加exits事件，否则不能监听到节点创建事件
        zooKeeper.exists("/test", true);
        // 创建一个临时anyone权限的节点(临时节点不能创建子节点)
        String s = zooKeeper.create("/test", "haha".getBytes(), digest, EPHEMERAL);
        System.out.println(s);

        // 开启另外一个客户端，使用不对的密码，查看是否可以查询到数据
        getDataBadAcl();

        getDataCorrecAcl();

        zooKeeper.close();
    }

    public static void getDataBadAcl() throws Exception{
        ZooKeeper zooKeeper = null;
        try {

            ZookeeperNativeApiForAcl zookeeperNativeApi = new ZookeeperNativeApiForAcl();
            zooKeeper = new ZooKeeper(SERVER_IP_PORT, 3000, zookeeperNativeApi);
            // 添加用户
            zooKeeper.addAuthInfo("digest", "1234567".getBytes());
            zookeeperNativeApi.countDownLatch.await();
            byte[] data = zooKeeper.getData("/test", true, null);
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zooKeeper.close();
        }

    }

    public static void getDataCorrecAcl() throws Exception{
        ZooKeeper zooKeeper = null;
        try {

            ZookeeperNativeApiForAcl zookeeperNativeApi = new ZookeeperNativeApiForAcl();
            zooKeeper = new ZooKeeper(SERVER_IP_PORT, 3000, zookeeperNativeApi);
            // 添加用户
            zooKeeper.addAuthInfo("digest", "123456".getBytes());
            zookeeperNativeApi.countDownLatch.await();
            byte[] data = zooKeeper.getData("/test", true, null);
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zooKeeper.close();
        }

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
                    System.out.println("会话id:" + zooKeeper.getSessionId());
                    zooKeeper.exists(path, true);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
