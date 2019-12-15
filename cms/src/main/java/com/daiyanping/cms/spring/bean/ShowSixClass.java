package com.daiyanping.cms.spring.bean;

public abstract class ShowSixClass {

    public void showsix() {
        getPeople().showsix();
    }

//    @Lookup("woman")
    //不一定是抽象的
    public abstract People getPeople();
}
