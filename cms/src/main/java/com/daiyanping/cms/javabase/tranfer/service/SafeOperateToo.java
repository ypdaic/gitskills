package com.daiyanping.cms.javabase.tranfer.service;


import com.daiyanping.cms.javabase.tranfer.UserAccount;

import java.util.Random;

/**
 *
 *
 *类说明：不会产生死锁的安全转账第二种方法
 */
public class SafeOperateToo implements ITransfer {

    @Override
    public void transfer(UserAccount from, UserAccount to, int amount)
            throws InterruptedException {
        Random r = new Random();
        while(true){
            if(from.getLock().tryLock()){
                System.out.println(Thread.currentThread().getName()
                        +" get"+from.getName());
                try{
                    if(to.getLock().tryLock()){
                        try{
                            System.out.println(Thread.currentThread().getName()
                                    +" get"+to.getName());
                            from.flyMoney(amount);
                            to.addMoney(amount);
                            System.out.println(from);
                            System.out.println(to);
                            break;
                        }finally{
                            to.getLock().unlock();
                        }
                    }
                }finally {
                    from.getLock().unlock();
                }

            }
            //Thread.sleep(r.nextInt(2));
        }

    }
}
