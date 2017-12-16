package com.hd.utils.git.pojo;

import java.util.LinkedList;

/**
 * 项目节点类
 * @author jihang
 * @date 2017/12/15
 */

public class ProjectNode extends AbstractNode {

    public RootNode parent;
    public LinkedList<TaskNode> childList;

    public ProjectNode(String nodeName, Long nodeId) {
        name = nodeName;
        id = nodeId;
        type = NodeType.PROJECT.getType();
        version = 1;
        repoPath = null;
        parent = null;
        childList = new LinkedList<>();
    }
}