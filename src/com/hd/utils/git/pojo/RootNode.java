package com.hd.utils.git.pojo;

import com.hd.utils.git.business.Task;

import java.util.LinkedList;

/**
 * 树根类
 * @author jihang
 * @date 2017/12/15
 */

public class RootNode extends AbstractNode {

    /**单例类*/
    private static class LazyHolder {
        private static final RootNode RN = new RootNode();
    }
    public static RootNode getRoot() {
        return RootNode.LazyHolder.RN;
    }
    private RootNode() {
        name = "ROOT";
        id = (long)0;
        type = NodeType.ROOT.getType();
        version = 0;
        repoPath = "F:\\";
        childList = new LinkedList<>();
    }

    public LinkedList<ProjectNode> childList;

    public ProjectNode findProjectById(Long nodeId) {
        for(ProjectNode pn : childList) {
            if(nodeId.equals(pn.id)) {
                return pn;
            }
        }
        return null;
    }

    public TaskNode findTaskById(Long nodeId) {
        for(ProjectNode pn : childList) {
            for(TaskNode tn : pn.childList) {
                if(nodeId.equals(tn.id)) {
                    return tn;
                }
            }
        }
        return null;
    }

}