package cn.enjoyedu.ch1.vola;

import cn.enjoyedu.tools.SleepTools;

/**
 * 类说明：演示Volatile的提供的可见性
 */
public class VolatileCase {
//    private volatile static boolean ready;
    private static boolean ready;
    private static int number;

    //
    private static class PrintThread extends Thread{
        @Override
        public void run() {
            System.out.println("PrintThread is running.......");
//            while(!ready);//无限循环
                // ready为普通变量，但我们使用println方法时，可以察觉到
//            while退出了，这是因为println使用了锁，锁的内存语义如下
//            当线程释放锁时，JMM会把该线程对应的本地内存中的共享变量刷新到主内存中。
//            当线程获取锁时，JMM会把该线程对应的本地内存置为无效。从而使得被监视器保护的临界区代码必须从主内存中读取共享变量。
            while (!ready) {
                    System.out.println("number = "+number);
                }


            System.out.println("number = "+number);
        }
    }

    public static void main(String[] args) {
        new PrintThread().start();
        SleepTools.second(1);
        number = 51;
        ready = true;
        SleepTools.second(5);
        System.out.println("main is ended!");
    }
}
