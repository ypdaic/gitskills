package com.daiyanping.cms.javabase;

import java.util.Arrays;

/**
 * @ClassName BubblingAlgorithm
 * @Description TODO 冒泡排序
 * @Author daiyanping
 * @Date 2019-10-16
 * @Version 0.1
 */
public class BubblingAlgorithm {

    public static void main(String[] args) {

        int[] ints = bubblingAlgorithm(new int[]{2, 3, 5,0, 6, 90, 34, 67, 12, 23, 899, 91});
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
            
        }

    }

    /**
     * 排序过程
     * [2, 3, 0, 5, 6, 34, 67, 12, 23, 90, 91, 899]
     * [2, 0, 3, 5, 6, 34, 12, 23, 67, 90, 91, 899]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 91, 899]
     * [0, 2, 3, 5, 6, 12, 23, 34, 67, 90, 91, 899]
     * @param array
     * @return
     */
    public static int[] bubblingAlgorithm(int[] array) {
        int[] newArrays = new int[array.length];
        System.arraycopy(array, 0, newArrays, 0, array.length);

        for (int i = 1; i < newArrays.length; i++) {

            // 但循环到中间时就已经完成了排序，就没必要继续循环了
            boolean flag = true;

            /**
             * 每次循环一次后，下次循环少一次，是因为每次循环后能将最大值排在最后面，所以循环选出最大值后，下次循环就可以减少一次
             */
            for (int j = 0; j < newArrays.length - i; j++) {

                int before = newArrays[j];
                int after = newArrays[j + 1];
                if (before > after) {
                    newArrays[j] = after;
                    newArrays[j + 1] = before;
                    flag = false;
                }
            }
            System.out.println(Arrays.toString(newArrays));

            if (flag) {
                break;
            }

        }

        return newArrays;

    }
}
