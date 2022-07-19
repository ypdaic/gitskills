package com.daiyanping.cms.zookeeper.enjoy.lock;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Classname ZkImproveLockImpl
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 21:16
 * Version 1.0
 */
public class ZkImproveLockImpl extends ZkAbstractLock {
    //记录当前客户端创建的临时节点
    private String currentPath;

    //记录上一个节点
    private String beforePath;

    private CountDownLatch cdl;

    public ZkImproveLockImpl() {
        if(!client.exists(path)) {
            client.createPersistent(path,"");
        }
    }

    @Override
    protected boolean tryLock() {
        if (currentPath == null || currentPath.length() <= 0) {
            // /lock/0000000001
            currentPath = client.createEphemeralSequential(path + "/", "");
        }

        //拿到/lock下面的所有儿子节点
        List<String> children = client.getChildren(path);
        Collections.sort(children);
        //children.get(0) 就是最小的那个节点
        if (currentPath.equals(path + "/" + children.get(0))) {
            return true;
        } else {
            //如果不是第一个，那么就必须找出当前节点的上一个节点
            //找到当前节点在所有子节点的索引
            int i = Collections.binarySearch(children, currentPath.substring(6));
            beforePath = path + "/" + children.get(i - 1);
        }
        return false;
    }

    @Override
    protected void waitforlock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
            //一旦/lock节点被删除以后，就会触发这个方法
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                //让等待的代码不再等待了
                if (cdl != null) {
                    cdl.countDown();
                }
            }
        };
        //每一个客户端就只需要注册对前一个节点的监听
        client.subscribeDataChanges(beforePath, iZkDataListener);

        if (client.exists(beforePath)) {
            cdl = new CountDownLatch(1);
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        client.unsubscribeDataChanges(beforePath, iZkDataListener);
    }
}
