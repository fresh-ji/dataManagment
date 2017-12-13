package com.hd.utils.git.Pojo;

import java.util.LinkedList;

/**
 * Created by jihang on 2017/11/20.
 */

public class TreeNode {

    private String name;//名字
    private int id;//id，作为唯一识别
    public String repoPath;//带名字的全路径
    public TreeNode parent;//任务的项目节点
    public LinkedList<TreeNode> childList;//项目的任务列表

    public TreeNode(String name, int nodeId) {
        this.name = name;
        this.id = nodeId;
        repoPath = null;
        parent = null;
        childList = new LinkedList<>();
    }

    //针对本工程的两层结构，希望写成遍历算法
    public static TreeNode findNodeById(TreeNode root, int nodeId) {
        for(TreeNode tn : root.childList) {
            if(tn.id == nodeId){
                return tn;
            }
            for(TreeNode tnn : tn.childList){
                if(tnn.id == nodeId){
                    return tnn;
                }
            }
        }
        return null;
    }

    //针对本工程的两层结构，希望写成遍历算法
    public static TreeNode findNodeByName(TreeNode root, String nodeName) {
        for(TreeNode tn : root.childList) {
            if(tn.name.equals(nodeName)){
                return tn;
            }
            for(TreeNode tnn : tn.childList){
                if(tnn.name.equals(nodeName)){
                    return tnn;
                }
            }
        }
        return null;
    }

    //展示工程树接口，针对本工程的两层结构，希望写成遍历算法
    public static void StructureInfo(TreeNode root) {
        System.out.println("Tree structure is : ");
        for(TreeNode tn : root.childList) {
            System.out.println("Project " + tn.name + " has tasks : ");
            for(TreeNode tnn : tn.childList){
                System.out.println(tnn.name);
                System.out.println(tnn.id);
            }
        }
    }

}