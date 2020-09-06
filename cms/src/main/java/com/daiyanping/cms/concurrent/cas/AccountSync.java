package com.daiyanping.cms.concurrent.cas;

/**
 * sync
 * result :0
 *
 */
public class AccountSync implements Account {

    private Integer balance;


    public AccountSync(int balance){
        this.balance=balance;
    }

    @Override
    public Integer query() {
        return this.balance;
    }

    @Override
    public void acquire(Integer i) {
        synchronized (this) {
            this.balance -= i;
        }
    }
}
