package com.daiyanping.cms.ratelimit;

public class Leaky {

    public static class LeakyDemo {
        public long timeStamp = getNowTime();

        private long getNowTime() {
            return System.currentTimeMillis();
        }

        public int capacity; // 桶的容量
        public int rate; // 水漏出的速度
        public long water; // 当前水量(当前累积请求数)
        public boolean grant() {
            long now = getNowTime();
            // (now - timeStamp) * rate 计算本次请求到上次一次请求应该漏了多少水
            // water - (now - timeStamp) * rate 计算剩余水量
            water = Math.max(0L, water - (now - timeStamp) / 1000 * rate); // 先执行漏水，计算剩余水量
            timeStamp = now;
            // 如果剩余水量小于总水量放行
            if ((water + 1) < capacity) {
                // 尝试加水,并且水还未满
                water += 1;
                return true;
            }
            else {
                // 水满，拒绝加水
                return false;
            }
        }
    }
}
