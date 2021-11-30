package com.daiyanping.cms.aop;

import java.util.LinkedList;

public class LinkedTest {

    public static void main(String[] args) {
        LinkedList<Integer> firstLinked = new LinkedList<>();
        firstLinked.add(0, 2);
        firstLinked.add(1, 4);
        firstLinked.add(2, 3);

        LinkedList<Integer> secondLinked = new LinkedList<>();
        secondLinked.add(0, 5);
        secondLinked.add(1, 6);
        secondLinked.add(2, 4);
        test(firstLinked, secondLinked);

    }

    public static LinkedList<Integer> test(LinkedList<Integer> first, LinkedList<Integer> second) {
        int size = first.size();
        Integer firstNum = 0;
        for (int i = 0; i < size; i++) {
            Integer integer = first.get(i);
            int num = 0;
            if (i == 0) {
                num = integer;
            } else {

                num = integer * 10 * 10;
            }
            firstNum = firstNum + num;

        }
        System.out.println(firstNum);
        return null;

    }
}
