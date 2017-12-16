package com.hd.utils.git.pojo;

/**分支节点类
 * @author jihang
 * @date 2017/12/15
 */

public class BranchNode extends AbstractNode {

    public TaskNode parent;

    public BranchNode(String nodeName, Long nodeId) {
        name = nodeName;
        id = nodeId;
        type = NodeType.BRANCH.getType();
        version = 1;
        repoPath = null;
        parent = null;
    }
}