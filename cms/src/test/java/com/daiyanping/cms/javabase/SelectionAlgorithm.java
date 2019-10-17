package com.daiyanping.cms.javabase;

import java.util.Arrays;

/**
 * @ClassName SelectionAlgorithm
 * @Description TODO 选择排序
 * @Author daiyanping
 * @Date 2019-10-16
 * @Version 0.1
 */
public class SelectionAlgorithm {

    /**
     * 总是将最小值放到最前面，然后再次寻找最小值
     * @param args
     */
    public static void main(String[] args) {

        int[] ints = selectionAlgorithm(new int[]{2, 3, 5,0, 6, 90, 34, 67, 12, 23, 899, 91});
        System.out.println("--------------------------");
        int[] ints2 = select(new int[]{2, 3, 5,0, 6, 90, 34, 67, 12, 23, 899, 91});
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);

        }

    }

    /**
     * 排序过程
     * [0, 3, 5, 2, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 5, 3, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 12, 90, 67, 34, 23, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 90, 67, 34, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 90, 67, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 91, 899]
     * @param array
     * @return
     */
    public static int[] selectionAlgorithm(int[] array) {
        int[] newArrays = new int[array.length];
        System.arraycopy(array, 0, newArrays, 0, array.length);

        for (int i = 0; i < newArrays.length - 1; i++) {


            for (int j = i + 1; j < newArrays.length; j++) {

                int before = newArrays[i];
                int after = newArrays[j];
                if (before > after) {
                    newArrays[i] = after;
                    newArrays[j] = before;
                }

            }

            System.out.println(Arrays.toString(newArrays));



        }

        return newArrays;

    }

    /**
     *
     * @param sourceArray
     * @return
     */
    public static int[] select(int[] sourceArray) {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        // 总共要经过 N-1 轮比较
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;

            // 每轮需要比较的次数 N-i
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    // 记录目前能找到的最小值元素的下标
                    min = j;
                }
            }

            // 将找到的最小值和i位置所在的值进行交换
            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }

            System.out.println(Arrays.toString(arr));

        }
        return arr;
    }
}
