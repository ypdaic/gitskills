package com.daiyanping.cms.spring.datasource;

public class DynamicDataSourceHolder {

    private static ThreadLocal<String> local = new ThreadLocal<String>();

    public static String getDs() {
        return local.get();
    }

    public static ThreadLocal getLocal() {
        return local;
    }
}
