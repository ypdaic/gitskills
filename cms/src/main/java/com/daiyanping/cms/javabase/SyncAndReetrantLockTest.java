package com.daiyanping.cms.javabase;

public class SyncAndReetrantLockTest {

    private static Object object = new Object();

    /**
     * public class com.daiyanping.cms.javabase.SyncAndReetrantLockTest {
     *   public com.daiyanping.cms.javabase.SyncAndReetrantLockTest();
     *     Code:
     *        0: aload_0
     *        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *        4: return
     *
     *   public static void main(java.lang.String[]);
     *     Code:
     *        0: getstatic     #2                  // Field object:Ljava/lang/Object;
     *        3: dup
     *        4: astore_1
     *        5: monitorenter
     *        6: aload_1
     *        7: monitorexit 正常退出
     *        8: goto          16
     *       11: astore_2
     *       12: aload_1
     *       13: monitorexit 异常退出
     *       14: aload_2
     *       15: athrow
     *       16: return
     *     Exception table:
     *        from    to  target type
     *            6     8    11   any
     *           11    14    11   any
     *
     *   static {};
     *     Code:
     *        0: new           #3                  // class java/lang/Object
     *        3: dup
     *        4: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *        7: putstatic     #2                  // Field object:Ljava/lang/Object;
     *       10: return
     * }
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 1 加锁释放公平
         * sync 是非公平锁
         * reentrantlock 两者都可以
         *
         * 2 使用方法
         *   synchronized 不需要用户手动去释放锁，当sync执行代码完后，系统会自动让线程释放对锁的占用
         *   ReetrantLock 则需要用户手动释放锁若没有主动释放锁，就有可能导致出现死锁的现象
         *
         * 3 等待释放可中断
         *   sync 不可中断，除非抛异常或正常执行完成
         *   ReetrantLock 可中断，1，设置超时时间
         *                       2，lockInterruptibly() 方法获取锁， 使用interrupt() 方法 可以中断
         * 4 锁绑定多个条件Condition
         *  sync 没有，notify 只能随机唤醒，全部唤醒
         *  ReetrantLock 可以绑定多个Condition, 可以精确唤醒，获取全部唤醒
         */
        synchronized (object) {

        }

    }
}
