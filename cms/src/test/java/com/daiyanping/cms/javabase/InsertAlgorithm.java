package com.daiyanping.cms.javabase;

import java.util.Arrays;

/**
 * @ClassName InsertAlgorithm
 * @Description TODO 插入算法
 * @Author daiyanping
 * @Date 2019-10-16
 * @Version 0.1
 */
public class InsertAlgorithm {

    /**
     * 总是将最小值放到最前面，然后再次寻找最小值
     * @param args
     */
    public static void main(String[] args) {

        int[] ints = insertAlgorithm(new int[]{2, 3, 5,0, 6, 90, 34, 67, 12, 23, 899, 91});

        System.out.println("--------------------------\r\n");

        int[] ints2 = insert(new int[]{2, 3, 5,0, 6, 90, 34, 67, 12, 23, 899, 91});
//        for (int i = 0; i < ints.length; i++) {
//            System.out.println(ints[i]);
//
//        }

    }

    /**
     * 排序过程
     * [2, 3, 5, 0, 6, 90, 34, 67, 12, 23, 899, 91]
     * [2, 3, 5, 0, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 34, 90, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 34, 67, 90, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 12, 34, 67, 90, 23, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 91, 899]
     * @param array
     * @return
     */
    public static int[] insertAlgorithm(int[] array) {
        int[] newArrays = new int[array.length];
        System.arraycopy(array, 0, newArrays, 0, array.length);

        for (int i = 0; i < newArrays.length - 1; i++) {

            int postion = newArrays[i + 1];
            for (int j = i; j >= 0; j--) {
                int temp = newArrays[j];
                if (postion < newArrays[j]) {
                    newArrays[j] = postion;
                    newArrays[j + 1] = temp;
                }

            }

            System.out.println(Arrays.toString(newArrays));



        }

        return newArrays;

    }

    /**
     * 排序过程
     * [2, 3, 5, 0, 6, 90, 34, 67, 12, 23, 899, 91]
     * [2, 3, 5, 0, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 90, 34, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 34, 90, 67, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 34, 67, 90, 12, 23, 899, 91]
     * [0, 2, 3, 5, 6, 12, 34, 67, 90, 23, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 899, 91]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 91, 899]
     * @param array
     * @return
     */
    public static int[] insert(int[] array) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(array, array.length);

        // 从下标为1的元素开始选择合适的位置插入，因为下标为0的只有一个元素，默认是有序的
        for (int i = 1; i < arr.length; i++) {

            // 记录要插入的数据
            int tmp = arr[i];

            // 从已经排序的序列最右边的开始比较，找到比其小的数
            int j = i;
            while (j > 0 && tmp < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }

            // 存在比其小的数，插入
            if (j != i) {
                arr[j] = tmp;
            }

            System.out.println(Arrays.toString(arr));


        }
        return arr;
    }
}
