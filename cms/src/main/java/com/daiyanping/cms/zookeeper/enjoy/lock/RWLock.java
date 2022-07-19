package com.daiyanping.cms.zookeeper.enjoy.lock;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.client.ZooKeeperSaslClient;

import javax.net.ssl.X509KeyManager;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Classname RWLock
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 21:41
 * Version 1.0
 */
public class RWLock {
    private String root;
    private ZkClient zkClient;
    private String R = "r";
    private String W = "w";
    private String rLockpath;
    private String wLockpath;

    public void initClient(String url, String root) {
        this.root = root;
        zkClient = new ZkClient(url);
        if (!zkClient.exists(root)) {
            zkClient.createPersistent(root);
        }
    }

    /**
     * 是否获取到锁
     */
    public void rLock() {
        CountDownLatch rcdl = new CountDownLatch(1);
        // /RWLock/r-0000000001
        String rLockName = root + "/" + R + "-";
        //创建临时有序节点
        rLockpath = zkClient.createEphemeralSequential(rLockName, "");
        //拿到所有子节点
        List<String> children = zkClient.getChildren(root);
        sort(children);

        int rIndex = 0;
        for (int i = children.size() - 1; i >= 0; i--) {
            if (rLockpath.equals(root + "/" + children.get(i))) {
                //在这里记录下来当前节点的索引
                rIndex = i;
            } else if (i < rIndex && children.get(i).split("-")[0].equals(W)) {
                //当前的节点的前面节点有写节点
                //要等待
                //要注册离我最近的那个写
                zkClient.subscribeChildChanges(root + "/" + children.get(i), (parentPath, currentChilds) -> rcdl.countDown());
                try {
                    rcdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void unRLock() {
        if (this.rLockpath != null) {
            zkClient.delete(rLockpath);
            rLockpath = "";
        }
    }

    public void wLock() {
        CountDownLatch wcdl = new CountDownLatch(1);
        // /RWLock/r-0000000001
        String wLockName = root + "/" + W + "-";
        //创建临时有序节点
        wLockpath = zkClient.createEphemeralSequential(wLockName, "");
        //拿到所有子节点
        List<String> children = zkClient.getChildren(root);
        sort(children);

        for (int i = children.size() - 1; i >= 0; i--) {
            if (wLockpath.equals(root + "/" + children.get(i))) {
                //只有当i = 0的情况下才获取到锁
                if (i > 0) {
                    //那么这个当前写的节点不是第一个,那么由于读写互斥的原因，当前节点要等待
                    //只要等待前面那个节点
                    zkClient.subscribeChildChanges(root + "/" + children.get(i - 1), (parentPath, currentChilds) -> wcdl.countDown());
                    try {
                        wcdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public void unWLock() {
        if(this.wLockpath != null) {
            zkClient.delete(wLockpath);
            wLockpath = "";
        }
    }

    private void sort(List<String> nodes) {
        nodes.sort(Comparator.comparing(o -> o.split("-")[1]));
    }
}
