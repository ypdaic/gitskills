package com.daiyanping.cms.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @ClassName CuratorApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-21
 * @Version 0.1
 */
public class CuratorApi {

    public static void main(String args[]) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1", 3000, 5000, retryPolicy);
        curatorFramework.start();
    }
}
