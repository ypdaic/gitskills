package com.daiyanping.cms.AQS;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ConcurrentHashMapTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-29
 * @Version 0.1
 */
public class ConcurrentHashMapTest {

    private static final ConcurrentHashMap map = new ConcurrentHashMap<String, Object>();

    /**
     * new ConcurrentHashMap<String, Object>(); 只是确定sizeCtrl的大小，只有当使用put方法时，才开始初始化table
     * 如果多个线程同时put,使用cas机制只会有一个线程进行初始table,并设置sizeCtrl为-1，其他线程检测到sizeCtrl为-1就不会进行初始化，
     * 而是使用循环机制再次尝试往table中填充数据，如果索引上没有Node节点数据，也是使用cas机制保证只有一个线程添加，后续线程再次往该
     * 索引上添加节点数据时，会使用synchronized锁住首节点，（区分是链表还是红黑树）然后在将新节点放到队尾，如果链表长度大于 等于8后，就开始将链表转化为
     * 红黑树，在循环中如果检测到map在扩容，当前线程会协助扩容
     * @param args
     */
    public static void main(String[] args) {
        map.put("test", "test");
    }
}
