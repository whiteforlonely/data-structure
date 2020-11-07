package com.ake.structure.redblacktree;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public class RedBlackTree {

    private TreeNode<Integer> root;

    // 新增节点
    public void addNode(int value) {
        // 先把插入的节点标记为红色
        TreeNode<Integer> node =new TreeNode<Integer>(value, true);
        if (root == null) {
        // 如果是根节点，就直接插入，并且置为黑色
            node.setRed(false);
            root = node;
            return;
        }
        // 找到父节点
        TreeNode<Integer> currNode = root;
        TreeNode<Integer> uncleNode = null;
        // uncle节点为黑色时候的情况标记
        while (true) {
            if (value < currNode.getValue()) {
                // 左节点
                if (currNode.getLeft() == null) {
                    node.setParent(currNode);
                    currNode.setLeft(node);
                    break;
                } else {
                    // 标记叔叔节点
                    uncleNode = currNode.getRight();
                    currNode = currNode.left;
                }
            } else if (value > currNode.getValue()) {
                // 右节点
                if (currNode.getRight() == null) {
                    node.setParent(currNode);
                    currNode.setRight(node);
                    break;
                } else {
                    uncleNode = currNode.getLeft();
                    currNode = currNode.getRight();
                }
            }
        }
        // 只要判断父节点和祖父节点情况
        node.setUncle(uncleNode);
        // 判断父节点的颜色
        while (currNode != null && currNode.isRed()) {
            // 如果是红颜色，需要变色
            // 这边总共是2种情况，总的两种情况是根据 uncle节点颜色来决定怎么处理的
            if (uncleNode == null || !uncleNode.isRed()) {
                int blackUncleRule = 0;
                // 记录原来的结构并且进行判断
                TreeNode<Integer> gNode = currNode.getParent();
                if (gNode.getLeft() == currNode && currNode.getLeft() == node) {
                    blackUncleRule = 3;
                } else if (gNode.getLeft() == currNode && currNode.getRight() == node) {
                    blackUncleRule = 2;
                } else if (gNode.getRight() == currNode && currNode.getLeft() == node) {
                    blackUncleRule = 1;
                }
                // 这边就是叔叔节点是黑色的话，需要分成四种情况
                if (blackUncleRule == 3) {
                    // 左左
                    currNode.setRed(false);
                    gNode.setRed(true);
                    // 右旋操作
                    rightTransfer(gNode, currNode, uncleNode, node, currNode.getRight());

                    // 需要重新验证
                    node = currNode;
                    currNode = node.getParent();
                    uncleNode = node.getUncle();
                } else if (blackUncleRule == 2) {
                    // 左右
                    gNode.setRed(true);
                    node.setRed(false);
                    // 先左旋再右旋
                    leftTransfer(currNode, currNode.getLeft(), node, node.getLeft(), node.getRight());
                    rightTransfer(gNode, node, uncleNode, currNode, node.getRight());

                    //重新验证
                    currNode = node.getParent();
                    uncleNode = node.getUncle();
                } else if (blackUncleRule == 0) {
                    // 右右
                    currNode.setRed(false);
                    gNode.setRed(true);
                    // 左旋
                    leftTransfer(gNode, uncleNode, currNode, currNode.getLeft(), node);

                    // 重新验证
                    node = currNode;
                    currNode = node.getParent();
                    uncleNode = node.getUncle();
                } else {
                    // 右左
                    node.setRed(false);
                    gNode.setRed(true);
                    rightTransfer(currNode, node, currNode.getRight(), node.getLeft(), node.getRight());
                    leftTransfer(gNode, uncleNode, node, node.getLeft(), currNode);

                    // 重新验证
                    currNode = node.getParent();
                    uncleNode = node.getUncle();
                }
            } else {
                // 需要循环变色
                TreeNode<Integer> tmpNode = node;
                tmpNode.getParent().setRed(false);
                tmpNode.getUncle().setRed(false);
                if (tmpNode.getParent().getParent() != null && tmpNode.getParent().getParent() != root) {
                    // 把祖父节点标记成红色
                    tmpNode.getParent().getParent().setRed(true);

                    // 这种情况需要重新验证
                    node = tmpNode.getParent().getParent();
                    currNode = node.getParent();
                    uncleNode = node.getUncle();
                }
            }
        }

    }

    // 删除节点
    public void removeNode(int value){

    }

    // 左旋
    private void leftTransfer(TreeNode<Integer> gNode, TreeNode<Integer> leftNode, TreeNode<Integer> rightNode, TreeNode<Integer> rlNode, TreeNode<Integer> rrNode){
        if (gNode.getParent() != null) {
            boolean left = gNode.getParent().getLeft() == gNode;
            rightNode.setParent(gNode.getParent());
            if (left) {
                rightNode.getParent().setLeft(rightNode);
            } else {
                rightNode.getParent().setRight(rightNode);
            }
        } else {
            // gnode为root节点
            root = rightNode;
       }

        // 设置孩子
        gNode.setRight(rlNode);
        rightNode.setLeft(gNode);

        // 设置父节点
        gNode.setParent(rightNode);
        if (rlNode !=null) {
            rlNode.setParent(gNode);
        }
        rightNode.setParent(null);

        // 设置叔叔节点
        TreeNode<Integer> gNodeUncle = gNode.getUncle();
        gNode.setUncle(rightNode.getUncle());
        rightNode.setUncle(gNodeUncle);
        if (leftNode !=null) {
            leftNode.setUncle(rightNode.getRight());
        }
        if (rrNode !=null) {
            rrNode.setUncle(gNode.getUncle());
        }
        if (rlNode != null) {
            rlNode.setUncle(rrNode);
        }

    }

    // 右旋
    private void rightTransfer(TreeNode<Integer> gNode, TreeNode<Integer> leftNode, TreeNode<Integer> rightNode, TreeNode<Integer> llNode, TreeNode<Integer> lrNode){
        if (gNode.getParent() != null) {
            boolean left = gNode.getParent().getLeft() == gNode;
            leftNode.setParent(gNode.getParent());
            if (left) {
                rightNode.getParent().setLeft(rightNode);
            } else {
                rightNode.getParent().setRight(rightNode);
            }
        } else {
            root = leftNode;
        }

        // 设置孩子
        gNode.setLeft(lrNode);
        leftNode.setRight(gNode);

        // 设置父节点
        gNode.setParent(leftNode);
        if (lrNode != null) {
            lrNode.setParent(gNode);
        }
        leftNode.setParent(null);

        // 设置叔叔节点
        TreeNode<Integer> gNodeUncle = gNode.getUncle();
        gNode.setUncle(leftNode.getUncle());
        leftNode.setUncle(gNodeUncle);
        if (rightNode !=null) {
            leftNode.setUncle(leftNode.getLeft());
        }
        if (llNode !=null) {
            llNode.setUncle(gNode.getUncle());
        }
        if (lrNode != null) {
            lrNode.setUncle(llNode);
        }
    }

    /* =============检查各种规则=============
        1. 根节点是黑色的
        2. 不能有相邻的两个红色节点
        3. 到所有叶子节点的黑色节点数是一样的
     */
    private boolean isTwoRedNodeCombined(){
        return false;
    }

    private boolean hasSameBlackNodeNum(){
        return false;
    }

    private void print1(TreeNode<Integer> node){
        if (node == null) {
            return;
        }
        System.out.println("[" + node.getValue() + "]" + node.isRed());

        if (null != node.getLeft()) {
            print1(node.getLeft());
        }
        if (null != node.getRight()) {
            print1(node.getRight());
        }
    }

    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        for (int i = 0; i < 9; i ++) {
            tree.addNode(i);
        }

        tree.print1(tree.root);
    }
}
