package com.hd.utils.git.handle;

import com.hd.utils.git.pojo.*;
import com.hd.utils.git.business.Project;
import com.hd.utils.git.business.Task;
import com.hd.utils.git.responsetype.CommitInfo;
import com.hd.utils.git.responsetype.ForkInfo;
import com.hd.utils.git.common.ServerResponse;

import java.util.List;

/**
 * 请求处理类
 * @author jihang
 * @date 2017/11/22
 */

public class RequestProcess {

    /**添加项目->接口*/
    public static ServerResponse addProject(String projectName, Long projectId) throws Throwable {
        Project pj = new Project();
        ServerResponse sr = pj.addCargo(Long.toString(projectId), pj.projectPath);
        if(sr.isSuccess()) {
            ProjectNode node = new ProjectNode(projectName, projectId);
            node.setRepoPath(pj.projectPath + Long.toString(projectId));
            node.parent = RootNode.getRoot();
            RootNode.getRoot().childList.push(node);
        }
        return sr;
    }

    /**添加任务->接口*/
    public static ServerResponse addTask(String taskName, Long taskId, Long projectId) throws Throwable {
        Task ta = new Task();
        ProjectNode parent = RootNode.getRoot().findProjectById(projectId);
        if(parent == null) {
            return ServerResponse.createByErrorMessage("project id not found!");
        }
        //这里的cargo是root端
        ServerResponse sr = ta.addCargo(Long.toString(taskId),
                parent.getRepoPath() + "/");
        if(sr.isSuccess()) {
            TaskNode node = new TaskNode(taskName, taskId);
            //这里的repo是master端
            node.setRepoPath(ta.taskPath + Long.toString(taskId));
            node.parent = parent;
            parent.childList.push(node);
        }
        return sr;
    }

    //目前不支持删除操作
    //public Integer deleteProject(Long projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectHash);
    //    pj.deleteCargo(node.repoPath);
    //    node.childList.clear();
    //    root.childList.remove(node);
    //    return 1;
    //}

    //public Integer deleteTask(Long taskId) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = findNode(taskHash);
    //    ta.deleteCargo(node.repoPath);//删除master端
    //    ta.deleteCargo(node.parent.repoPath
    //            + "/" + node.name);
    //    node.parent.childList.remove(node);
    //    return 1;
    //}

    //目前不支持项目的fork
    //public ServerResponse<ForkInfo> forkProject(String userIden, int projectId) throws Throwable {
    //   Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    return pj.forkCargo(userIden, node.repoPath);
    //}

    /**fork任务->接口*/
    public static ServerResponse<ForkInfo> forkTask(String userIdentify, Long taskId) throws Throwable {
        Task ta = new Task();
        TaskNode node = RootNode.getRoot().findTaskById(taskId);
        if(node == null) {
            return ServerResponse.createByErrorMessage(
                    "Unable to fork, task id not found");
        }
        ServerResponse<ForkInfo> sr = ta.forkCargo(userIdentify, node.getRepoPath());
        if(sr.isSuccess()) {
            BranchNode bNode = new BranchNode(sr.getData().branchName, (long)1);
            bNode.parent = node;
        }
        return sr;
    }


    //目前不支持项目的getCommitChannel
    //public ServerResponse<List<CommitInfo>> getProjectCommitChannel(int projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    return pj.getCommitChannel(node.repoPath, timeProgress);
    //}

    /**获取任务各分支最新更改->接口*/
    //public ServerResponse<List<CommitInfo>> getTaskCommitChannel(Long taskId) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = TreeNode.findTaskById(root, taskId);
    //    if(node == null) {
    //        return ServerResponse.createByErrorMessage(
    //                "Unable to get commit channel, task id not found");
    //    }
    //    return ta.getCommitChannel(node.repoPath);
    //}

    //目前不支持项目的lookChange
    //public ServerResponse<String> lookProjectFile(String fileName, int commitHash, int projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    return pj.lookFile(fileName, commitHash, node.repoPath);
    //}

    //获取任务某分支commit某文件更改结果->接口
    //public ServerResponse<ChangeInfo> checkTaskChange(String fileName, int commitHash, int taskId) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = TreeNode.findTaskById(root, taskId);
    //    if(node == null) {
    //        return ServerResponse.createByErrorMessage(
    //                "Unable to check task change, task id not found");
    //    }
    //    return ta.checkChange(fileName, commitHash, node.repoPath);
    //}

    //目前不支持项目的merge
    //public ServerResponse<Integer> processProjectCommit(int commitHash, int projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    ServerResponse<Integer> sr = pj.mergeCargo(commitHash, node.repoPath);
    //    return sr;
    //}


    /**任务按照commit合并分支->接口*/
    //public ServerResponse processTaskCommit(Integer commitHash, Long taskId) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = TreeNode.findTaskById(root, taskId);
    //    if(node == null) {
    //        return ServerResponse.createByErrorMessage(
    //                "Unable to process commit, task id not found");
    //    }
    //    return ta.mergeCargoByCommit(commitHash, node.repoPath);
    //}

    //目前不支持任务向项目推送
    //Task向Project推送->接口，只有Task才有，和上一个都是merge过程，返回值应该是boolean
    //public void pushTask(int taskId) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = findNode(taskId);
    //    ta.pushTask(node.repoPath, node.parent.repoPath + "/" + node.name);
    //}

}