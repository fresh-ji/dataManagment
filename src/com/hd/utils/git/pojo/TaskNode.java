package com.hd.utils.git.pojo;

import java.util.LinkedList;

/**任务节点类
 * @author jihang
 * @date 2017/12/15
 */

public class TaskNode extends AbstractNode {

    public ProjectNode parent;
    public LinkedList<DataNode> dataList;
    public LinkedList<BranchNode> branchList;

    public TaskNode(String nodeName, Long nodeId) {
        name = nodeName;
        id = nodeId;
        type = NodeType.TASK.getType();
        version = 1;
        repoPath = null;
        parent = null;
        dataList = new LinkedList<>();
        branchList = new LinkedList<>();
    }
}