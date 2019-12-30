package com.daiyanping.cms.STM;


/**
 * @ClassName VersionedRef
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-12-30
 * @Version 0.1
 */
public final class VersionedRef<T> {

    final T value;

    final long version;

    //构造方法
    public VersionedRef(T value, long version) {
        this.value = value;
        this.version = version;
    }
}
