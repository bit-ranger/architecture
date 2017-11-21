package com.rainyalley.architecture.core.arithmetic;

public class MergeSort {


    public <T extends Comparable<T>> void sort(T[] arr){
        // 一个子数组的长度
        // 从 1开始分割，与递归不同的是，递归由数组长度一分为二最后到1，
        // 而非递归则是从1开始扩大二倍直到数组长度
        int size = 1;

        while (size < arr.length) {
            // 完全二叉树一层内的遍历
            for (int left = 0; left + size <= arr.length - 1; left += size * 2) {
                //中间地址，即右数组的起始地址
                int mid = left + size - 1;
                //右数组的尾部
                int end = left + size * 2 - 1;

                // 防止超出数组长度
                if (end > arr.length - 1){
                    end = arr.length - 1;
                }

                // 合并
                merge(arr, left, mid, end);
            }

            // 下一层
            size *= 2;
        }
    }


    /**
     * 合并并排序
     * @param arr 待排序数组
     * @param left 左数组起点
     * @param right 右数组起点
     * @param end 右数组终点
     */
    private <T extends Comparable<T>> void merge(T[] arr, int left, int right, int end) {
        Object[] temp = new Object[end - left + 1];

        //左边数组元素的位置
        int i = left;
        //右边数组元素的位置
        int j = right + 1;
        //temp数组元素的位置
        int k = 0;

        // 注意： 此处并没有全部放入temp中，当一边达到mid或right时就是退出循环
        while (i <= right && j <= end) {
            //如果左边元素更小，就放入temp，位置+1
            if (arr[i].compareTo(arr[j]) < 0){
                temp[k++] = arr[i++];
            }
            //如果右边元素更小，就放入temp，位置+1
            else{
                temp[k++] = arr[j++];
            }
        }

        // 如果左边或右边有剩余，则继续放入，只可能一边有剩余
        while (i <= right){
            temp[k++] = arr[i++];
        }

        while (j <= end){
            temp[k++] = arr[j++];
        }


        // 排好序的临时数组重新放入原数组
        for (int m = 0; m < temp.length; m++) {
            arr[m + left] = (T)temp[m];
        }
    }

}
