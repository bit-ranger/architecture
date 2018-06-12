package com.rainyalley.architecture.arithmetic.tree;

public class BTree<T extends Comparable<T>> {

    public ValueItem<T> find(BTreeNode<T> root, T value){
        BTreeNode<T> cNode = root;
        ValueItem<T> cItem = cNode.getFirst();
        while (true){
            if(cItem.getValue().compareTo(value) == 0){
                break;
            } else if(cItem.getValue().compareTo(value) < 0){
                if(cItem.getNext() == null){
                    if(cItem.getRight() == null){
                        cNode = null;
                        cItem = null;
                        break;
                    } else {
                        cNode = cItem.getRight();
                        cItem = cNode.getFirst();
                    }
                }
            } else {
                if(cItem.getLeft() == null){
                    cNode = null;
                    cItem = null;
                    break;
                } else {
                    cNode = cItem.getLeft();
                    cItem = cNode.getFirst();
                }
            }
        }
        if(cItem == null){
            return null;
        } else {
            return cItem;
        }
    }


}
