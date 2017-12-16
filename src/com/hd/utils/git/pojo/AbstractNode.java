package com.hd.utils.git.pojo;

/**
 * 树节点基类
 * @author jihang
 * @date 2017/11/20
 */

abstract class AbstractNode {

    /**节点名*/
    String name;
    /**节点id，作为唯一识别*/
    Long id;
    /**节点类型*/
    Integer type;
    /**节点版本*/
    Integer version;
    /**节点对应的库目录路径，为带名字的全路径*/
    String repoPath;

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    public Integer getVersion() {
        return version;
    }

    public void updateVersion() {
        version++;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public String getRepoPath() {
        return repoPath;
    }

}