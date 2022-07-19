package com.daiyanping.cms.zookeeper.enjoy.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname OrderNumFactory
 * @Description TODO
 * @Author Jack
 * Date 2021/6/17 20:10
 * Version 1.0
 */
public class OrderNumFactory {

    private static int i = 0;

    public String createOrderNum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        return simpleDateFormat.format(new Date()) + ++i;
    }
}
