package com.daiyanping.cms.lock;

public class LockThreadLocal<T> {

	public final ThreadLocal<T> threadLocal = new ThreadLocal<T>();

	public T getT() {
		return threadLocal.get();
	}

	public void setT(T t) {
		threadLocal.set(t);
	}

	public void clean() {
		threadLocal.remove();
	}
}
