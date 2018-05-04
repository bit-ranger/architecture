package com.rainyalley.architecture.arithmetic.tree;

import lombok.Data;

@Data
public class ValueItem<T extends Comparable<T>> {

    private T value;

    private ValueItem<T> next;

    private BTreeNode<T> left;

    private BTreeNode<T> right;
}
