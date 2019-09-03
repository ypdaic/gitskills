package com.daiyanping.cms.javabase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @ClassName ArrayListTest
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-03
 * @Version 0.1
 */
public class ArrayListTest {

    public static void main(String[] args ) {
        ArrayList<String> list = new ArrayList<>();
        list.add("dd");
        // 虽然此时数组大小为10，但是通过指定索引添加数据时，索引只能小于等于数据实际size，而不是数组的length
//        list.add(2, "dd");
        list.add("aa");
//        remove 方法只是将数据从数据当中移除，size会变化，而数组的length并不会变化
        list.remove("dd");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {

            // remove 方法，必须在next方法执行后执行，remove方法执行后，会重新移除的位置开始遍历
            iterator.remove();
        }
    }
}
