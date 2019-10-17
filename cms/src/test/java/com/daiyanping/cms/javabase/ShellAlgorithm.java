package com.daiyanping.cms.javabase;

import java.util.Arrays;

/**
 * @ClassName ShellAlgorithm
 * @Description TODO 希尔排序，插入排序的优化
 * @Author daiyanping
 * @Date 2019-10-17
 * @Version 0.1
 */
public class ShellAlgorithm {

    public static void main(String[] args) {
         shell(new int[]{2, 3, 5,0, 6, 90, 34, 67, 12, 23, 899, 91});
    }

    public static void shell(int[] sourceArray) {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int gap = 1;
        while (gap < arr.length) {
            gap = gap * 3 + 1;
        }

        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) {
                int tmp = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > tmp) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                    System.out.println(Arrays.toString(arr));
                }
                arr[j + gap] = tmp;
                System.out.println(Arrays.toString(arr));
            }
            gap = (int) Math.floor(gap / 3);

        }

    }
}
