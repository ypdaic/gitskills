package com.daiyanping.cms.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MysqlDistributedLockTest {

	private static int count = 0;

	private static int threadNum = 50;

//	private static Lock lock2 = new MysqlDistributedLockWithUptate();

//	private static final Lock lock2 = new MysqlDistributedLockWithInster();

	private static  Lock lock2 = new MysqlDistributeLockWithSelect();

	//发令枪，让多个线程一起等待，线程数满后，一起释放.这里等待一百线程，然后一起执行
	private static final CountDownLatch countDownLatch = new CountDownLatch(threadNum);

	//在多个线程结尾进行拦截，所有线程都执行完了，才一起结束
	private static final CyclicBarrier cyc = new CyclicBarrier(threadNum + 1);

	//用于存放多个锁，更加细粒度的锁
	private static final ConcurrentHashMap<String, Lock> map = new ConcurrentHashMap<String, Lock>();

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < threadNum; i++) {

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
					if (count <= 15) {

						countInAdd("lock1");
					}
					else if (count > 15 && count < 30) {

						countInAdd("lock2");
					}
					else {

						countInAdd("lock3");
					}
					try {
						//等所有线程执行完毕后，才继续往下执行，否则等待
						cyc.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			});

			thread.start();


		}

		try {
			cyc.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();

		System.out.println("所有线程执行完毕需要的时间：" + (endTime - startTime));
	}

	/**
	 * 50个线程只用一个锁大概要2636毫秒（mysql insert方式）(inster mysql使用的是表锁)
	 * 50个线程用三个锁大概要2761毫秒（mysql insert方式）(inster mysql使用的是表锁)
	 * 50个线程只用一个锁大概要2746毫秒 （mysql update方式）（update 如果有索引则使用行锁，否则使用表锁）
	 * 50个线程只用3个锁大概要2683毫秒 （mysql update方式）（update 如果有索引则使用行锁，否则使用表锁）
	 * 50个线程只用一个锁大概要2636毫秒 （mysql select for update方式）（select for update 如果有索引则使用行锁，否则使用表锁）
	 * 在使用连接池的情况，大致都差不多
	 * update方式一个或多个锁差不多，问题还是在于mysql最终是否使用行级锁，是引擎说了算，就算我们给了索引
	 * @param lock
	 */
	public static void countInAdd(String lock) {
//		lock2.getLock("lock1");
		getLock(lock);
		try {
			count ++;
			System.out.println(Thread.currentThread().getName() + " 锁名：" +lock + "=" + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			unLock(lock);
//			lock2.unLock("lock1");
		}
	}

	public static void getLock(String lock) {
		//如果key对应的值存在，则获取值，如果不存在存入新值
//		Lock lock1 = map.putIfAbsent(lock, new MysqlDistributedLockWithInster());
//		Lock lock1 = map.putIfAbsent(lock, new MysqlDistributedLockWithUptate());
		Lock lock1 = map.putIfAbsent(lock, new MysqlDistributeLockWithSelect());
		if (lock1 == null) {
//			map.put(lock, new MysqlDistributedLockWithInster());
//			map.put(lock, new MysqlDistributedLockWithUptate());
			map.put(lock, new MysqlDistributeLockWithSelect());
		}

		Lock lock2 = map.get(lock);
		lock2.getLock(lock);
	}

	public static void unLock(String lock) {
		Lock lock2 = map.get(lock);
		lock2.unLock(lock);
	}
}
