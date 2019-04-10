package com.daiyanping.cms.lock;

import java.util.concurrent.CountDownLatch;

public class MysqlDistributedLockTest {

	private static int count = 0;

	private static Lock lock = new MysqlDistributedLock();

	//发令枪，让多个线程一起等待，线程数满后，一起释放.这里等待一百线程，然后一起执行
	private static CountDownLatch countDownLatch = new CountDownLatch(50);

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					countDownLatch.countDown();
					long count = countDownLatch.getCount();
					Thread.currentThread().setName("线程名：" + count);
					try {
						System.out.println("开始等待：" + count);
						countDownLatch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					countInAdd(count);
				}
			});

			thread.start();


		}
	}

	public static void countInAdd(long i) {
		lock.getLock("test1");
		try {
			count ++;
			System.out.println(Thread.currentThread().getName() + "=" + i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unLock("test1");
		}
	}
}
