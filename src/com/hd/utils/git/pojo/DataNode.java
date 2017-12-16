package com.hd.utils.git.pojo;

/**数据文件节点类
 * @author jihang
 * @date 2017/12/15
 */

public class DataNode extends AbstractNode {

    public TaskNode parent;

    public class Data {
        private String name;
        private String author;
        private String time;
        private String content;
        private Integer version;
    }

    public DataNode(String nodeName, Long nodeId) {
        name = nodeName;
        id = nodeId;
        type = NodeType.DATA.getType();
        version = 1;
        repoPath = null;
        parent = null;
    }
}