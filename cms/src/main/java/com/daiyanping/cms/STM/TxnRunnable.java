package com.daiyanping.cms.STM;

/**
 * @ClassName TxnRunnable
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-30
 * @Version 0.1
 */
@FunctionalInterface
public interface TxnRunnable {

    void run(Txn txn);
}
