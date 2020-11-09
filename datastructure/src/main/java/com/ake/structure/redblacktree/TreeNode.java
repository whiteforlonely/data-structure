package com.ake.structure.redblacktree;

import lombok.Data;

@Data
public class TreeNode<T> {

    T value;
    TreeNode<T> left;
    TreeNode<T> right;
    TreeNode<T> parent;
    boolean red;
    int hash;

    public TreeNode(T value, boolean red){
        this.red = red;
        this.value = value;
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer(value.toString());
        if (red) {
            buffer.append("r");
        } else {
            buffer.append("b");
        }
        return buffer.toString();
    }
}
