package com.daiyanping.cms.AQS;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DelayQueueTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-29
 * @Version 0.1
 */
public class DelayQueueTest {

    public static void main(String[] args) {
        DelayQueue<MyDelay> myDelays = new DelayQueue<>();
        myDelays.put(new MyDelay(10));
        try {
            long l = System.currentTimeMillis();
            System.out.println();
            MyDelay take = myDelays.take();
            System.out.println(take);
            System.out.println(System.currentTimeMillis() - l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyDelay implements Delayed {

        // 过期时间，毫秒为单位
        private long activeTime;

        public MyDelay(long activeTime) {
            this.activeTime = activeTime * 1000 + System.currentTimeMillis();
        }

        // 重新计算过期时间，单位是纳秒
        @Override
        public long getDelay(TimeUnit unit) {
            long d = unit.convert((this.activeTime
                    -System.currentTimeMillis() ) * 1000 * 1000,unit);
            System.out.println(d);
            return d;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }
}
