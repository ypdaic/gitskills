package com.daiyanping.cms.javabase;

/**
 * 可见性验证
 */
public class VolatileSeeOkTest {

    /**
     * 验证JMM的内存可见性，由于number不是volatile的
     * 导致主线程感知不到number变化了
     * 当number 使用volatile修饰后就保证了number的可见性
     * @param args
     */
    public static void main(String[] args) {
        MyData myData = new MyData();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "启动");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo60();
            System.out.println("线程更新" + myData.getNumber());
        }, "aaa").start();

        while (myData.getNumber() == 0) {

        }

        System.out.println("主线程结束");
    }

    public static class MyData {

        private volatile int number = 0;

        public void addTo60() {
            number = 60;
        }

        public int getNumber() {
            return this.number;
        }
    }
}
