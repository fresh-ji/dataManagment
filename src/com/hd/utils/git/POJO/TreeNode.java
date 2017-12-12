package com.hd.utils.git.POJO;

import java.util.LinkedList;

/**
 * Created by jihang on 2017/11/20.
 */

public class TreeNode {

    public String name;
    public String repoPath;
    public TreeNode parent;
    public int idCode;
    public LinkedList<TreeNode> childlist;

    public TreeNode(String name) {
        this.name = name;
        repoPath = null;
        parent = null;
        childlist = new LinkedList<>();
    }
}