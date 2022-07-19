package com.daiyanping.cms.zookeeper.enjoy.zkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

/**
 * @Classname ZkClientDemo
 * @Description TODO
 * @Author Jack
 * Date 2021/6/15 21:32
 * Version 1.0
 */
public class ZkClientDemo {
    private static String connectStr = "192.168.67.139:2184";

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient(connectStr, 5000);

        String path = "/demo2";
        if(!zkClient.exists(path))
        zkClient.createPersistent(path,"jack");

        System.out.println(zkClient.readData(path).toString());

        //事件监听
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath + "数据变更" + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        });

        //要跟服务端通讯
        zkClient.writeData(path,"Jack1");

        TimeUnit.SECONDS.sleep(1);
        zkClient.writeData(path,"Jack2");
        TimeUnit.SECONDS.sleep(1);

        System.out.println(zkClient.readData(path).toString());

        zkClient.deleteRecursive("/node5");


    }
}
