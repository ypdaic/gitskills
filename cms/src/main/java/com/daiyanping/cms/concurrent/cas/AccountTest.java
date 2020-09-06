package com.daiyanping.cms.concurrent.cas;

import java.util.ArrayList;
import java.util.List;

/**
 * zl
 */
public class AccountTest {

    public static void main(String[] args) throws InterruptedException {
        test(new AccountNomal(10000));
        //test(new AccountSync(10000));
//       test(new AccountCas(10000));
    }

    /**
     * 传入一个具体的Account来测试
     * @param account
     */
    static void test(Account account) throws InterruptedException {
        List<Thread> ts = new ArrayList<>();
        //定义500个线程 每个线程针对account取20
        for (int i = 0; i < 500; i++) {
            ts.add(new Thread(() -> {
                account.acquire(20);
            }));
        }
        //启动
        for (Thread t : ts) {
            t.start();
        }
        //等待他们全部指向完
        for (Thread t : ts) {
            t.join();
        }
        //查询还剩多少
        System.out.println(account.query());
    }
}
