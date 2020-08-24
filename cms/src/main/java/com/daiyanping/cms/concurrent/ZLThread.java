package com.daiyanping.cms.concurrent;

public class ZLThread {

    //装载库，保证JVM在启动的时候就会装载，故而一般是也给static
    static {
        System.loadLibrary( "EnjoyThreadNative" );
    }


    public native void start1();

    public static void main(String[] args) {
        ZLThread zlThread = new ZLThread();
        zlThread.start1();
    }
}
