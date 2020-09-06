package com.daiyanping.cms.concurrent.cas;

/**
 * 没有锁没有CAS
 * 大于等于100
 *
 */
public class AccountNomal implements Account {
    //余额
    private Integer balance;
    public AccountNomal(int balance){
        this.balance=balance;
    }

    @Override
    public Integer query() {
        return this.balance;
    }

    @Override
    public void acquire(Integer i) {

        this.balance -= i;
    }
}
