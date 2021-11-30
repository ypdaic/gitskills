package com.daiyanping.cms.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static javafx.scene.input.KeyCode.X;
import static javafx.scene.input.KeyCode.Y;

public class ThreadTest {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition conditionX = reentrantLock.newCondition();
        Condition conditionY = reentrantLock.newCondition();
        Condition conditionZ = reentrantLock.newCondition();

        new Thread(() -> {

            for (int i = 0; i < 10; i++) {
                reentrantLock.lock();
                try {
                    conditionX.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("X");
                try {
                    conditionY.signal();
                } finally {
                    reentrantLock.unlock();
                }

            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                reentrantLock.lock();
                try {
                    conditionY.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("Y");
                try {
                    conditionZ.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                reentrantLock.lock();
                try {
                    conditionZ.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("Z ");
                try {
                    conditionX.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }
        }).start();
        reentrantLock.lock();
        try {
            conditionX.signal();
        } finally {
            reentrantLock.unlock();
        }
    }



}
