package com.daiyanping.cms.STM;

/**
 * @ClassName Txn
 * @Description TODO 事物管理器
 * @Author daiyanping
 * @Date 2019-12-30
 * @Version 0.1
 */
public interface Txn {

    /**
     * 从当前事物中获取数据
     * @param ref
     * @param <T>
     * @return
     */
    <T> T get(TxnRef<T> ref);

    /**
     * 修改当前事物的数据
     * @param ref
     * @param value
     * @param <T>
     */
    <T> void set(TxnRef<T> ref, T value);
}
