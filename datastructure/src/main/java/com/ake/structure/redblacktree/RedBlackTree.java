package com.ake.structure.redblacktree;

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
        if (node.getParent() == null) {
            // 没有可以插入的地方，因为是重复的值，核心外的东西。
            return;
        }
        // 只要判断父节点和祖父节点情况
        // 判断父节点的颜色
        while (currNode != null && currNode.isRed() && node.isRed()) {
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
                    uncleNode = getUncle(node);
                } else if (blackUncleRule == 2) {
                    // 左右
                    gNode.setRed(true);
                    node.setRed(false);
                    // 先左旋再右旋
                    leftTransfer(currNode, currNode.getLeft(), node, node.getLeft(), node.getRight());
                    rightTransfer(gNode, node, uncleNode, currNode, node.getRight());

                    //重新验证
                    currNode = node.getParent();
                    uncleNode = getUncle(node);
                } else if (blackUncleRule == 0) {
                    // 右右
                    currNode.setRed(false);
                    gNode.setRed(true);
                    // 左旋
                    leftTransfer(gNode, uncleNode, currNode, currNode.getLeft(), node);

                    // 重新验证
                    node = currNode;
                    currNode = node.getParent();
                    uncleNode = getUncle(node);
                } else {
                    // 右左
                    node.setRed(false);
                    gNode.setRed(true);
                    rightTransfer(currNode, node, currNode.getRight(), node.getLeft(), node.getRight());
                    leftTransfer(gNode, uncleNode, node, node.getLeft(), currNode);

                    // 重新验证
                    currNode = node.getParent();
                    uncleNode = getUncle(node);
                }
            } else {
                // 需要循环变色
                TreeNode<Integer> tmpNode = node;
                tmpNode.getParent().setRed(false);
                TreeNode<Integer> tmpUncleNode = getUncle(node);
                if (null != tmpUncleNode) {
                    tmpUncleNode.setRed(false);
                }
                if (tmpNode.getParent().getParent() != null && tmpNode.getParent().getParent() != root) {
                    // 把祖父节点标记成红色
                    tmpNode.getParent().getParent().setRed(true);

                    // 这种情况需要重新验证
                    node = tmpNode.getParent().getParent();
                    currNode = node.getParent();
                    uncleNode = getUncle(node);
                }
            }
        }
    }

    // 获取叔叔节点
    private TreeNode<Integer> getUncle(TreeNode<Integer> node){
        TreeNode<Integer> pNode = node.getParent();
        if (pNode == null) {
            return null;
        }
        TreeNode<Integer> gNode = pNode.getParent();
        if (gNode == null) {
            return null;
        }

        if (gNode.getLeft() == pNode) {
            return gNode.getRight();
        } else {
            return gNode.getLeft();
        }
    }

    // 获取兄弟节点
    private TreeNode<Integer> getBrother(TreeNode<Integer> node) {
        if (node == null) {
            return null;
        }
        TreeNode<Integer> pNode = node.getParent();
        if (pNode == null) {
            return null;
        }
        if (pNode.getLeft() == node) {
            return pNode.getRight();
        }
        return pNode.getLeft();
    }

    // 删除节点
    public void removeNode(int value){
        // 红黑树的删除涉及到的地方应该是有很多的
        // 1. 找到对应节点
        if (root == null) {
            return;
        }
        TreeNode<Integer> currNode = root;
        while (currNode != null && currNode.getValue() != value) {
            if (value < currNode.getValue()) {
                currNode = currNode.getLeft();
            } else if (value > currNode.getValue()) {
                currNode = currNode.getRight();
            }
        }
        // 最后还是没有找到
        if (currNode == null) {
            System.err.println("can not find deleted node");
        }

        // 开始删除节点
        TreeNode<Integer> removedNode = currNode;
        // 二叉树 删除操作

        // 模拟删除二叉树
        while (removedNode != null) {

            if (removedNode.getLeft() == null && removedNode.getRight() == null) {
                if (!removedNode.isRed()) {
                    // 删除的是黑色的节点，需要进行平衡
                    removedNode = reblanceTree(removedNode);
                }
                // 删除的节点是红色的，并且是无子节点的
                if ((removedNode.getParent().getLeft() == removedNode)) {
                    removedNode.getParent().setLeft(null);
                } else {
                    removedNode.getParent().setRight(null);
                }
                removedNode.setParent(null);
                removedNode = null;
            } else if (removedNode.getLeft() != null && removedNode.getRight() == null) {
                // 只有一个子节点
                if ((removedNode.getParent().getLeft() == removedNode)) {
                    removedNode.getParent().setLeft(removedNode.getLeft());
                } else {
                    removedNode.getParent().setRight(removedNode.getLeft());
                }
                removedNode.setParent(null);
                removedNode = null;
            } else if (removedNode.getRight() != null && removedNode.getLeft() == null) {
                // 只有一个子节点
                if ((removedNode.getParent().getLeft() == removedNode)) {
                    removedNode.getParent().setLeft(removedNode.getRight());
                } else {
                    removedNode.getParent().setRight(removedNode.getRight());
                }
                removedNode.setParent(null);
                removedNode = null;
            } else if (removedNode.getRight() != null && removedNode.getLeft() != null) {
                // 同时有两个子节点
                TreeNode<Integer> nextRemovedNode = getNextReplaceNode(removedNode);
                removedNode.setValue(nextRemovedNode.getValue());
                removedNode = nextRemovedNode;
            }
        }

        // END
    }

    // 获取后继节点
    private TreeNode<Integer> getNextReplaceNode(TreeNode<Integer> removedNode){
        if (removedNode == null) {
            return null;
        }
        TreeNode<Integer> left = removedNode.getLeft();
        TreeNode<Integer> right = removedNode.getRight();
        if (left == null && right == null) {
            return null;
        }
        if (left != null) {
            if (left.getRight() != null) {
                return left.getRight();
            } else {
                return left;
            }
        }
        if (right.getLeft() != null) {
            return right.getLeft();
        }
        return right;
    }

    // 删除时重新进行平衡
    private TreeNode<Integer> reblanceTree(TreeNode<Integer> currNode){

        TreeNode<Integer> removedNode = currNode;
        // 重新平衡红黑树
        while (currNode != null && currNode != root && !currNode.isRed()) {
            if (currNode.getParent().getLeft() == currNode) {
                // 兄弟为右孩子
                TreeNode<Integer> bNode = currNode.getParent().getRight();
                if (bNode.isRed()) {
                    // 兄弟节点为红色
                    bNode.setRed(false);
                    bNode.getParent().setRed(true);
                    leftTransfer(bNode.getParent(), currNode, bNode, bNode.getLeft(), bNode.getRight());
                } else {
                    // 兄弟节点为黑色
                    if (bNode.getLeft() != null && !bNode.getLeft().isRed() && bNode.getRight() != null && !bNode.getRight().isRed()
                            || bNode.getLeft() == null && bNode.getRight() == null) {
                        bNode.setRed(true);
                        // 根据父节点的颜色来做处理
                        if (currNode.getParent().isRed()) {
                            // 父亲节点是红色的时候
                            currNode.getParent().setRed(false);
                            break;
                        } else {
                            currNode = currNode.getParent();
                        }
                    } else if (bNode.getRight() !=  null && bNode.getRight().isRed()) {
                        // 兄弟节点的右孩子是红色的
                        // 交换颜色
                        boolean parentRed = bNode.getParent().isRed();
                        boolean bRed = bNode.isRed();
                        bNode.getParent().setRed(bRed);
                        bNode.setRed(parentRed);
                        // 兄弟节点右孩子涂黑
                        bNode.getRight().setRed(false);
                        // 父节点 左旋
                        leftTransfer(bNode.getParent(), currNode, bNode, bNode.getLeft(), bNode.getRight());
                    } else if (bNode.getRight() == null || !bNode.getRight().isRed()) {
                        // 兄弟的右孩子是黑色的
                        if (bNode.getLeft() != null) {
                            bNode.getLeft().setRed(false);
                        }
                        bNode.setRed(true);
                        // 兄弟节点 左旋
                        rightTransfer(bNode, bNode.getLeft(), bNode.getRight(), null, null);
                    }
                }
            } else {
                // 兄弟为左孩子
                TreeNode<Integer> bNode = currNode.getParent().getLeft();
                if (bNode.isRed()) {
                    // 兄弟节点为红色
                    bNode.setRed(false);
                    bNode.getParent().setRed(true);
                    rightTransfer(bNode.getParent(), bNode, currNode, bNode.getLeft(), bNode.getRight());
                } else {
                    // 兄弟节点为黑色
                    if (bNode.getLeft() != null && !bNode.getLeft().isRed() && bNode.getRight() != null && !bNode.getRight().isRed()
                    || bNode.getLeft() == null && bNode.getRight() == null) {
                        bNode.setRed(true);
                        // 根据父节点的颜色来做处理
                        if (currNode.getParent().isRed()) {
                            // 父亲节点是红色的时候
                            currNode.getParent().setRed(false);
                            break;
                        } else {
                            currNode = currNode.getParent();
                        }
                    } else if (bNode.getLeft() !=  null && bNode.getLeft().isRed()) {
                        // 兄弟节点的左孩子是红色的
                        // 交换颜色
                        boolean parentRed = bNode.getParent().isRed();
                        boolean bRed = bNode.isRed();
                        bNode.getParent().setRed(bRed);
                        bNode.setRed(parentRed);
                        // 兄弟节点左孩子涂黑
                        bNode.getLeft().setRed(false);
                        // 父节点 右旋
                        rightTransfer(bNode.getParent(), bNode, currNode, bNode.getLeft(), bNode.getRight());
                    } else if (bNode.getLeft() == null || !bNode.getLeft().isRed()) {
                        // 兄弟的左孩子是黑色的
                        if (bNode.getRight() != null) {
                            bNode.getRight().setRed(false);
                        }
                        bNode.setRed(true);
                        // 兄弟节点 左旋
                        leftTransfer(bNode, bNode.getLeft(), bNode.getRight(), null, null);
                    }
                }
            }
        }

        return removedNode;
    }

    // 二叉树的删除
    private TreeNode<Integer> bTreeDelete(TreeNode<Integer> removedNode) {
        TreeNode<Integer> x,y;
        if (removedNode.getLeft() == null || removedNode.getRight() == null) {
            y = removedNode;
        } else {
            y = removedNode.getRight();
        }
        if (null != y.getLeft()) {
            x = y.getLeft();
        } else {
            x = y.getRight();
        }

        x.setParent(y.getParent());

        if (y.getParent() == null) {
            root = x;
        } else if (y.getParent().getLeft() == y) {
            // 左孩子
            y.getParent().setLeft(x);
        } else {
            y.getParent().setRight(x);
        }

        if (removedNode != y) {
            removedNode.setValue(y.getValue());
        }
        if (!y.isRed()) {

        }
        return y;
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
            rightNode.setParent(null);
       }

        // 设置孩子
        gNode.setRight(rlNode);
        rightNode.setLeft(gNode);

        // 设置父节点
        gNode.setParent(rightNode);
        if (rlNode !=null) {
            rlNode.setParent(gNode);
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
            leftNode.setParent(null);
        }

        // 设置孩子
        gNode.setLeft(lrNode);
        leftNode.setRight(gNode);

        // 设置父节点
        gNode.setParent(leftNode);
        if (lrNode != null) {
            lrNode.setParent(gNode);
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
            if (i == 7) {
                System.out.println("test point");
            }
        }

        tree.removeNode(3);
        tree.removeNode(0);
        tree.removeNode(1);
        tree.removeNode(7);

        tree.print1(tree.root);
    }
}
