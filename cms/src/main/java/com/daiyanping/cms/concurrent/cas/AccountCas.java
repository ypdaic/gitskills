package com.daiyanping.cms.concurrent.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * cas
 * result :0
 *
 */
public class AccountCas implements Account {
    public AccountCas(int balance){
        this.balance=new AtomicInteger(balance);
    }

    private AtomicInteger balance;
    @Override
    public Integer query() {
        return this.balance.get();
    }

    @Override
    public void acquire(Integer i) {
            while(true) {
                //t1 prev=80
                int prev = balance.get();
                //t1 next 80   t2 80
                int next = prev - i;
                //t2  balance=80
                if(balance.compareAndSet(prev, next)) {
                    break;
                }
                //balance.getAndAdd(-1*i);

        }

    }
}
