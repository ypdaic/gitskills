package com.daiyanping.cms.lock;

public interface Lock {

	void  getLock(String lock);

	void unLock(String lock);
}
