package com.daiyanping.cms.jvm;

/**
 * @ClassName PretenureSizeThresholdTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/7/24
 * @Version 0.1
 */
public class PretenureSizeThresholdTest {

    /**
     * -XX:PretenureSizeThreshold=4m  PretenureSizeThreshold 参数只对 Serial 和 ParNew 两款收集器有效，指定大于该设置值的对象直接在老年代分配
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        byte[] bytes = new byte[1024 * 1024 * 4];
        Thread.sleep(1000000);
    }
}
