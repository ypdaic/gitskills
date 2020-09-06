package com.daiyanping.cms.ratelimit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeakyBucket {

    public class LeakBucket {
        /**
         * 时间
         */
        private long time;
        /**
         * 总量
         */
        private Double total;
        /**
         * 水流出去的速度
         */
        private Double rate;
        /**
         * 当前总量
         */
        private Double nowSize;

        public boolean limit() {
            long now = System.currentTimeMillis();
            // 否个时间内突然很多请求过来导致nowsize 不断变大，当大于最大时则拒绝，而nowSize的减小则是匀速的
            nowSize = Math.max(0, (nowSize - (now - time)  * rate));
            time = now;
            if ((nowSize + 1) < total) {
                nowSize++;
                return true;
            } else {
                return false;
            }

        }
    }
}
