package com.daiyanping.cms.lock;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 自己实现Lock锁
 */
public class KingLock implements Lock {

	//原子操作类
	private AtomicReference<Thread> atomicReference = new AtomicReference<>();

	//队列，用于保存排队的线程
	private LinkedBlockingQueue<Thread> linkedBlockingQueue = new LinkedBlockingQueue<>();

	/**
	 * 加锁
	 */
	@Override
	public void lock() {
		//使用cas检查当前值与内存值是否相等，如果相等就更新为预期值，这里预期值就是当前线程
		//这里与null相比，如果不为null说明已经有线程占用该锁了，必须排队了
		while (!atomicReference.compareAndSet(null, Thread.currentThread())) {
			//不相等就说明有有线程占用该锁，必须等待了
			linkedBlockingQueue.add(Thread.currentThread());
			//使用LockSupport.park();阻塞当前线程
			LockSupport.park();

			//阻塞释放后，就要在队列中删除
			linkedBlockingQueue.remove(Thread.currentThread());
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {

	}

	@Override
	public boolean tryLock() {
		//使用cas检查当前值与内存值是否相等，如果相等就更新为预期值，这里预期值就是当前线程
		//这里与null相比，如果不为null说明已经有线程占用该锁了,直接返回false
		if (!atomicReference.compareAndSet(null, Thread.currentThread())) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

		
		//使用cas检查当前值与内存值是否相等，如果相等就更新为预期值，这里预期值就是当前线程
		//这里与null相比，如果不为null说明已经有线程占用该锁了，必须排队了
		while (!atomicReference.compareAndSet(null, Thread.currentThread())) {
			//不相等就说明有有线程占用该锁，必须等待了
			linkedBlockingQueue.add(Thread.currentThread());
			//使用LockSupport.park();阻塞当前线程
			LockSupport.parkUntil(unit.toNanos(time));

		}
	}

	/**
	 * 释放锁
	 */
	@Override
	public void unlock() {
		//检查内存值是否与当前线程相等，相等就更新为null
		while (atomicReference.compareAndSet(Thread.currentThread(), null)) {
			Iterator<Thread> iterator = linkedBlockingQueue.iterator();
			while (iterator.hasNext()) {
				Thread next = iterator.next();
				//释放排队线程的阻塞状态，进入执行状态，这里会发生多个线程抢占同一个锁的情况，
				LockSupport.unpark(next);
			}
		}
	}

	@Override
	public Condition newCondition() {
		return null;
	}
}
