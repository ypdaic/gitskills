package com.daiyanping.cms.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper原生api
 */
public class ZookeeperTest {

	//ZooKeeper服务地址
	private static final String SERVER = "192.168.140.128:2181";

	//会话超时时间
	private static final int SESSION_TIMEOUT = 30000;

	/**
	 * 打印结果 都还没获取到会话id
	 * State:CONNECTING sessionid:0x0 local:null remoteserver:null lastZxid:0 xid:1 sent:0 recv:0 queuedpkts:0 pendingresp:0 queuedevents:0
	 * CONNECTING
	 * 这种方式客户端的连接状态还是CONNECTING，CONNECTED的状态才表示连接成功
	 */
	@Test
	public void test1() {
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(SERVER, SESSION_TIMEOUT,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(zooKeeper);
		System.out.println(zooKeeper.getState());
	}

	/**
	 * 使用CountDownLatch让主线程等待ZooKeeper客户端真正连接上服务器后再去获取状态
	 * 打印结果
	 * State:CONNECTED Timeout:30000 sessionid:0x6c9a9b51fc0000 local:/192.168.140.1:59655 remoteserver:192.168.140.128/192.168.140.128:2181 lastZxid:0 xid:1 sent:1 recv:1 queuedpkts:0 pendingresp:0 queuedevents:0
	 * CONNECTED
	 */
	@Test
	public void test2() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(SERVER, SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					countDownLatch.countDown();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(zooKeeper);
		System.out.println(zooKeeper.getState());
	}

	/**
	 * zookeeper原生api 增删查改
	 */
	@Test
	public void test3() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(SERVER, SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getState() == Event.KeeperState.SyncConnected) {
						System.out.println("客户端已连接服务器成功！");
						countDownLatch.countDown();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			// 创建一个ACL为"world", "anyone" 且拥有所有权限的节点
			zooKeeper.create("/test5", "haha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			byte[] data = zooKeeper.getData("/test5", false, null);
			System.out.println("获取节点数据" + new String(data));

			// 更新节点
            // 在ZooKeeper中，数据版本都是从0开始计数额，所以严格的讲，"-1"不是一个合法得到数据版本，它仅仅是一个标示符。
            // 如果客户端传入的版本参数是"-1"，就是告诉zookeeper服务器，客户端需要基于数据的最新版本进行更新操作。
			zooKeeper.setData("/test5", "更新数据".getBytes(), -1);
			byte[] data2 = zooKeeper.getData("/test5", false, null);
			System.out.println("获取更新节点数据" + new String(data2));

			// 删除节点
			zooKeeper.delete("/test5", -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			zooKeeper.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * zookeeper原生api 增删查改
	 */
	@Test
	public void test4() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(SERVER, SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getState() == Event.KeeperState.SyncConnected) {
						System.out.println("客户端已连接服务器成功！");
						countDownLatch.countDown();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		exitsToDelete(zooKeeper, "/test5/child");
		exitsToDelete(zooKeeper, "/test5");

		try {
			// 创建一个ACL为"world", "anyone" 且拥有所有权限的节点
			zooKeeper.create("/test5", "haha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			byte[] data = zooKeeper.getData("/test5", false, null);
			System.out.println("获取节点数据" + new String(data));

			// 更新节点
			// 在ZooKeeper中，数据版本都是从0开始计数额，所以严格的讲，"-1"不是一个合法得到数据版本，它仅仅是一个标示符。
			// 如果客户端传入的版本参数是"-1"，就是告诉zookeeper服务器，客户端需要基于数据的最新版本进行更新操作。
			zooKeeper.setData("/test5", "更新数据".getBytes(), -1);
			byte[] data2 = zooKeeper.getData("/test5", false, null);
			System.out.println("获取更新节点数据" + new String(data2));

			// 删除节点
			zooKeeper.delete("/test5", -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			zooKeeper.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存在节点就先删除
	 * @param zooKeeper
	 * @param path
	 */
	public static void exitsToDelete(ZooKeeper zooKeeper, String path) {
		try {
			Stat exists = zooKeeper.exists(path, false);
			if (exists != null) {
				zooKeeper.delete(path, -1);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
