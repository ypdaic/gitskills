package com.daiyanping.cms.zookeeper;

import org.apache.zookeeper.server.ZooKeeperServerMain;

/**
 * @ClassName ZkServerApi
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-22
 * @Version 0.1
 */
public class ZkServerApi {

    public static void main (String[] args) {
        String[] args2 = new String[3];
        args2[0] = "2182";
//        args2[1] = "/Users/daiyanping/git-clone-repository/gitskills/cms/zk-data";
        args2[1] = "D:\\gitskills\\cms\\zk-data";
        args2[2] = "300000000";
        ZooKeeperServerMain.main(args2);
    }
}
