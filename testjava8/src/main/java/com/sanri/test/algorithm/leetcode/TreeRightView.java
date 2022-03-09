package com.sanri.test.algorithm.leetcode;

/*
 问题：实现一个算法，能够给出二叉树的右视图，最终输出是一个数组
 给定一棵二叉树，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 输入: [1,2,3,null,5,null,4]
 输出: [1, 3, 4]
 解释:

   1            <---
 /   \
2     3         <---
 \     \
  5     4       <---

  1
  23
  4567
*/

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TreeRightView {
  
    public static class TreeNode {

        TreeNode left;
        TreeNode right;
        int val;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    List<Integer> nodes = new Vector<>();
    {
        nodes.add(1);
        nodes.add(2);
        nodes.add(3);
        nodes.add(null);
        nodes.add(5);
        nodes.add(null);
        nodes.add(4);
    }
    /**
     * 根据中序遍历的数组构建树结构
     * @param nodes
     * @return
     */
    public TreeNode buildTree(TreeNode root,List<Integer> nodes,int index){
        if (index > nodes.size() / 2){
            return root;
        }
        if (index == 1){
            if (nodes.get(0) != null)
            root.val = nodes.get(0);
        }
        if (nodes.get(index * 2 -1) != null) {
            root.left = new TreeNode(nodes.get(index * 2 - 1));
            buildTree(root.left,nodes,index + 1);
        }
        if (nodes.get(index * 2) != null) {
            root.right = new TreeNode(nodes.get(index * 2));
            buildTree(root.right, nodes, index + 2);
        }

        return root;
    }

    public List<Integer> rightView(TreeNode root) {
        List<Integer> nodes = new ArrayList<>();

        List<TreeNode> floor = new ArrayList<>();
        floor.add(root);

        while (!floor.isEmpty()){
            // 记录每层的最后一个节点
            nodes.add(floor.get(floor.size() - 1).val);

            // 记录下一层
            List<TreeNode> nextFloor = new ArrayList<>();
            for (int i = 0; i < floor.size(); i++) {
                TreeNode treeNode = floor.get(i);
                if (treeNode.left != null){
                    nextFloor.add(treeNode.left);
                }
                if (treeNode.right != null){
                    nextFloor.add(treeNode.right);
                }
            }
            floor = nextFloor;
        }


        return nodes;
    }

    @Test
    public void testRightView(){
        TreeNode treeNode = buildTree(new TreeNode(-1), nodes, 1);
        List<Integer> integers = rightView(treeNode);
        System.out.println(integers);
    }

    @Test
    public void testBuild(){
        TreeNode treeNode = buildTree(new TreeNode(-1), nodes, 1);
        System.out.println(treeNode);
    }

}