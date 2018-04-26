package com.rainyalley.architecture.arithmetic.sort;

/**
 * 外部归并排序
 * 使用循环实现
 * @author bin.zhang
 * @see DoubleWayReadStore 双路随机访问存储器
 * @see CachedStore 用文件实现的随机访问存储器
 */
public class DoubleWayReadStoreMergeSort {

    public <T extends Comparable<T>> void sort(DoubleWayReadStore<T> arr){
        // 一个子数组的长度
        // 从 1开始分割，与递归不同的是，递归由数组长度一分为二最后到1，
        // 而非递归则是从1开始扩大二倍直到数组长度
        long size = 1;

        while (size < arr.size()) {
            // 完全二叉树一层内的遍历
            for (long left = 0; left + size <= arr.size() - 1; left += size * 2) {
                //中间地址，即右数组的起始地址
                long right = left + size;
                //右数组的尾部
                long end = left + size * 2 - 1;

                // 防止超出数组长度
                if (end > arr.size() - 1){
                    end = arr.size() - 1;
                }

                // 合并
                merge(arr, left, right, end);
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
    protected  <T extends Comparable<T>> void merge(DoubleWayReadStore<T> arr, long left, long right, long end) {
        Store<T> temp = arr.fork(arr.name() + "_" + left + "_" + right + "_" + end, end - left + 1);

        //左边数组元素的位置
        long li = left;
        //右边数组元素的位置
        long ri = right;
        //temp数组元素的位置
        long ti = 0;

        // 注意： 此处并没有全部放入temp中，当一边达到mid或right时就是退出循环
        while (li < right && ri <= end) {
            //如果左边元素更小，就放入temp，位置+1
            if (arr.getLeftWay(li).compareTo(arr.getRightWay(ri)) < 0){
                temp.set(ti++, arr.getLeftWay(li++));
            }
            //如果右边元素更小，就放入temp，位置+1
            else{
                temp.set(ti++, arr.getRightWay(ri++));
            }
        }

        // 如果左边或右边有剩余，则继续放入，只可能一边有剩余
        while (li < right){
            temp.set(ti++, arr.getLeftWay(li++));
        }

        while (ri <= end){
            temp.set(ti++, arr.getRightWay(ri++));
        }


        // 排好序的临时数组重新放入原数组
        for (int m = 0; m < temp.size(); m++) {
            arr.set(m + left, temp.get(m));
        }

        temp.delete();
    }


}
