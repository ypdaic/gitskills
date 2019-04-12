package com.daiyanping.cms.zookeeper;

import org.I0Itec.zkclient.ZkClient;

public class ZookeeperTest {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181");
		zkClient.createEphemeral("/test");
	}
}
