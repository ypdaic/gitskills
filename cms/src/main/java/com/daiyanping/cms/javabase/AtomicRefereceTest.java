package com.daiyanping.cms.javabase;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用，存在ABA问题
 */
public class AtomicRefereceTest {

    public static void main(String[] args) {
        User user = new User();
        user.setName("zhangshan");
        User user1 = new User();
        user1.setName("lishi");
        AtomicReference<User> userAtomicReference = new AtomicReference<>();
        userAtomicReference.set(user);

        userAtomicReference.compareAndSet(user, user1);
        System.out.println(userAtomicReference.get().getName());
    }

    @Data
    public static class User {
        private String name;
    }
}
