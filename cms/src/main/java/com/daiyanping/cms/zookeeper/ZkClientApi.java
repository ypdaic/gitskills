package com.daiyanping.cms.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @ClassName ZkClientApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-21
 * @Version 0.1
 */
public class ZkClientApi {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.0.4:2182", 3000);

        // 由于这些事件的调用都是在另外的线程中调用，可能会出现某些事件触发不了，比如节点删除事件，
        // 在触发节点删除事件时，如果节点由被创建了是无法触发节点删除事件的
        // subscribeDataChanges监听了节点数据变化，节点创建，节点删除事件
        zkClient.subscribeDataChanges("/test", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                if (data == null) {

                    System.out.println(dataPath + "节点创建");
                }

                if (data != null) {
                    System.out.println(dataPath + "节点数据变化：" + data);
                }
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("节点删除" + dataPath);
            }
        });

//         subscribeChildChanges监听了子节点变化，节点创建，节点删除事件
//         该方法会调用exit,getChild
//        zkClient.subscribeChildChanges("/test", new IZkChildListener() {
//            @Override
//            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
//                if (CollectionUtils.isNotEmpty(currentChilds)) {
//
//                    System.out.println("子节点变更事件" + currentChilds);
//                }
//            }
//        });

        zkClient.subscribeDataChanges("/test/test2", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                if (data == null) {

                    System.out.println(dataPath + "子节点创建");
                }

                if (data != null) {
                    System.out.println(dataPath + "子节点数据变化：" + data);
                }
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("子节点删除" + dataPath);
            }
        });

        boolean exists = zkClient.exists("/test");
        if (exists) {
            // 这里会触发子节点事件
            zkClient.deleteRecursive("/test");
        }

        zkClient.createPersistent("/test", "hahah");

        zkClient.writeData("/test", "testtt");

        zkClient.createPersistent("/test/test2", false);
        zkClient.createPersistent("/test/test3", false);
//
//        // 子节点
        zkClient.writeData("/test/test2", "sfsfs");
//        zkClient.writeData("/test/test2", "sfssssfs");



        try {
            Thread.sleep(1000 * 60 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zkClient.close();
    }

}
