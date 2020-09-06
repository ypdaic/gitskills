package com.daiyanping.cms.ratelimit;

public class TokenBucket {

    public class TokenBucketDemo {
        public long timeStamp = getNowTime();

        private long getNowTime() {
            return System.currentTimeMillis();
        }

        public long capacity; // 桶的容量
        public long rate; // 令牌放入速度
        public long tokens; // 当前令牌数量
        public boolean grant() {
            long now = getNowTime();
            // (now - timeStamp) * rate 计算距离上次请求间隔内应该有多少个令牌可用
            // 和总令牌数去最小值，也就是获取可用令牌数
            tokens = Math.min(capacity, tokens + (now - timeStamp) * rate);
            timeStamp = now;
            if (tokens < 1) {
                // 若不到1个令牌,则拒绝
                return false;
            }
            else {
                 // 还有令牌，领取令牌
                tokens -= 1;
                return true;
            }
        }
    }
}
