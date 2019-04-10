package com.daiyanping.cms.lock;

public abstract class AbstractDistributedLock{

	public void getLock(String lock) {
		if (!getTryLock(lock)) {
			waitLock();
			getLock(lock);
		}
	}

	protected abstract boolean getTryLock(String lock);

	protected abstract void waitLock();

}
