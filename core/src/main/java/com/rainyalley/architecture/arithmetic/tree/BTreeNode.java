package com.rainyalley.architecture.arithmetic.tree;

import lombok.Data;

@Data
public class BTreeNode<T extends Comparable<T>> {

    private String filePath;

    private ValueItem<T> first;

}
