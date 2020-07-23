package com.daiyanping.cms.javabase;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @ClassName HashSetTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2020/6/11
 * @Version 0.1
 */
public class HashSetTest {

    /**
     * 当key的hashcode和equals都相等时，HashMap会认为完全相等的对象，只会触发value的新旧替换，而不会创建新的node节点
     * 当key的hashcode不相等时，创建一个新的node节点
     * 当key的hashcode相等时，equals不相等时，创建一个新的node形成链表
     * @param args
     */
    public static void main(String[] args) {
        HashSet<Test> tests = new HashSet<>();
        tests.add(new Test("1"));
        tests.add(new Test("2"));
        tests.forEach(test -> {
            System.out.println(test.getName());
        });
        Iterator<Test> iterator = tests.iterator();
        while (iterator.hasNext()) {
            Test next = iterator.next();
        }
    }

    @AllArgsConstructor
    @Data
    public static class Test {

        private String name;

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return true;
        }
    }
}
