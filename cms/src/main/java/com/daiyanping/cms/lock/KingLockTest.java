package com.daiyanping.cms.lock;

import java.util.concurrent.CountDownLatch;

public class KingLockTest {
	private static int count = 0;

	private static KingLock lock = new KingLock();

	//发令枪，让多个线程一起等待，线程数满后，一起释放.这里等待一百线程，然后一起执行
	private static CountDownLatch countDownLatch = new CountDownLatch(100);

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					countDownLatch.countDown();
					try {
						countDownLatch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					countInAdd();
				}
			});

			thread.start();


		}
	}

	public static void countInAdd() {
		lock.lock();
		try {
			count ++;
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}
