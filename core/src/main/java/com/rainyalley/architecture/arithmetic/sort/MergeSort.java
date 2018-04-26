package com.rainyalley.architecture.arithmetic.sort;

import java.util.Comparator;

/**
 * 归并排序
 * 使用循环实现
 */
public class MergeSort {


    public <T> void sort(T[] arr, Comparator<? super T> comparator){
        // 一个子数组的长度
        // 从 1开始分割，与递归不同的是，递归由数组长度一分为二最后到1，
        // 而非递归则是从1开始扩大二倍直到数组长度
        int size = 1;

        while (size < arr.length) {
            // 完全二叉树一层内的遍历
            for (int left = 0; left + size < arr.length; left += size * 2) {
                //右数组的起始地址
                int right = left + size;
                //右数组的尾部
                int end = left + size * 2 - 1;

                // 防止超出数组长度
                if (end > arr.length - 1){
                    end = arr.length - 1;
                }

                // 合并
                merge(arr, left, right, end, comparator);
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
    private <T> void merge(T[] arr, int left, int right, int end, Comparator<? super T> comparator) {
        Object[] temp = new Object[end - left + 1];

        //左边数组元素的位置
        int li = left;
        //右边数组元素的位置
        int ri = right;
        //temp数组元素的位置
        int ti = 0;

        // 注意： 此处并没有全部放入temp中，当一边达到mid或right时就是退出循环
        while (li < right && ri <= end) {
            //如果左边元素更小，就放入temp，位置+1
            if (comparator.compare(arr[li], arr[ri]) < 0){
                temp[ti++] = arr[li++];
            }
            //如果右边元素更小，就放入temp，位置+1
            else{
                temp[ti++] = arr[ri++];
            }
        }

        // 如果左边或右边有剩余，则继续放入，只可能一边有剩余
        if(li < right){
            System.arraycopy(arr, li, temp, ti, right-li);
        }

        if(ri <= end){
            System.arraycopy(arr, ri, temp, ti, end-ri+1);
        }


        // 排好序的临时数组重新放入原数组
        System.arraycopy(temp, 0, arr, left, temp.length);
    }

}
