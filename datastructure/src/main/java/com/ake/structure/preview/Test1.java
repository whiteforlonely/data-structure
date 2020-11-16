package com.ake.structure.preview;

import org.omg.CORBA.DATA_CONVERSION;

import java.util.Arrays;

/**
 * 25匹马，在5个跑道上跑，如何选出最近的前五名
 */
public class Test1 {
    public static void main(String[] args) {

        int[][] roadResult = {{34, 32, 20, 15, 10}, {45, 43, 41, 30, 20}, {25, 21, 20, 19, 15}, {40, 38, 34, 33, 10}, {50, 31, 30, 29, 11}};

        int[] road1 = roadResult[0].clone();

        // 上面只是基本的算法
//        for(int i=1; i < roadResult.length; i++) {
//            for (int j = 0;  j < roadResult[i].length; j++) {
//                step1: for (int m = 0; m < road1.length; m++) {
//                    if (roadResult[i][j] > road1[m]) {
//                        // insert data
//                        int k = m;
//                        int insertData = roadResult[i][j];
//                        while(k < road1.length) {
//                            int tmp = road1[k];
//                            road1[k] = insertData;
//                            insertData = tmp;
//                            k++;
//                        }
//                        break step1;
//                    }
//
//                }
//            }
//        }

        /*
        另外的算法，下载的问题是如何可以更快的找到前几名呢
        或者其中必有机关
        因为5个数组都是有顺序的，所以其实只要将5个数组合并成一个并进行重新排序，取出前5个就是最终结果
        现在关键的问题是应该用什么样的排序算法。
        因为冒泡，选择和插入排序的时间复杂度都是一样的最大，所以这边会考虑到时间复杂度会变化的排序算法。
        这边是这用希尔排序。
         */

        int[] data = new int[25];
        for (int i = 0; i < roadResult.length; i++) {
            for (int j = 0; j < roadResult[i].length; j++) {
                data[i*roadResult[i].length + j] = roadResult[i][j];
            }
        }
//        shellSort(data);
        quickSort(data, 0, data.length-1);
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + ",");
        }


        /*
        其实上面的已经排序的算法是适合归排序的，所以，还是需要理解所有的排序算法，才能真正的
        知道在实际中，应该怎么处理不同场景下的一个排序问题。
         */
        // 实际问题的真是算法
//        int[] result = new int[5];
//        int pos = 0;
//        int[] pointers = {0, 0, 0, 0, 0};
//        int needIncrPointerPos = 0;
//        int tmp = 0;
//        while (pos < 5) {
//            for (int i = 0; i < 5; i++) {
//                int tmpData = roadResult[i][pointers[i]];
//                if (tmpData > tmp) {
//                    tmp = tmpData;
//                    needIncrPointerPos = i;
//                }
//            }
//            pointers[needIncrPointerPos] = pointers[needIncrPointerPos] + 1;
//            result[pos++] = tmp;
//            tmp = 0;
//        }
//
//        for (int i = 0; i < road1.length; i++) {
//            System.out.print(result[i] + ",");
//        }
    }

    /**
     * 希尔排序是基于选择排序进行的，也算是低效的一种排序方案，现在看来，这样子也并不合适
     * 应对上面的排序结果。
     *
     * @param arr
     */
    public static void shellSort(int[] arr) {
        int length = arr.length;
        int temp;
        for (int step = length / 2; step >= 1; step /= 2) {
            for (int i = step; i < length; i++) {
                temp = arr[i];
                int j = i - step;
                while (j >= 0 && arr[j] < temp) {
                    arr[j + step] = arr[j];
                    j -= step;
                }
                arr[j + step] = temp;
            }
        }
    }

    /**
     * 快排的思想可能本身也是容易理解的，但是真正实行起来的话，各种细节的处理可能是需要注意的。
     *
     * @param data
     */
    public static void quickSort(int[] data, int start, int end){
        if (start < 0 || start >= data.length || end >= data.length || end < 0) {
            return;
        }

        int povit = data[end];
        int smallDataPos = -1;
        for (int i=start; i<= end; i++) {
            if (data[i] > povit && smallDataPos < 0) {
                smallDataPos = i;
            }
            if (data[i] <= povit && smallDataPos >=0) {
                int tmp = data[i];
                data[i] = data[smallDataPos];
                data[smallDataPos] = tmp;

                smallDataPos ++;
            }
        }
        int leftEnd = smallDataPos - 1;
        int rightStart = smallDataPos;
        // 划清排序边界
        while(leftEnd >= 0 && data[leftEnd] == povit) {
            leftEnd --;
        }
        while (rightStart >= 0 && rightStart <= end && data[rightStart] == povit) {
            rightStart ++;
        }
        quickSort(data, start, leftEnd);
        quickSort(data, rightStart, end);
    }
}
