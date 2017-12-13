package com.hd.utils.git.Handle;

import com.hd.utils.git.Pojo.Project;
import com.hd.utils.git.Pojo.Task;
import com.hd.utils.git.Pojo.TreeNode;
import com.hd.utils.git.ResponseType.ChangeInfo;
import com.hd.utils.git.ResponseType.CommitInfo;
import com.hd.utils.git.ResponseType.ForkInfo;
import com.hd.utils.git.Common.ServerResponse;

import java.util.List;

/**
 * Created by jihang on 2017/11/22.
 */

public class RequestProcess {

    //工程树根
    private TreeNode root;
    //commit处理时间进度
    //private int timeProgress;

    //使用单例模式
    private static class lazyHolder {
        private static final RequestProcess rq = new RequestProcess();
    }
    private RequestProcess() {
        root = new TreeNode("root", 0);
        //timeProgress = 0;
    }
    public static RequestProcess getInstance() {
        return lazyHolder.rq;
    }

    //添加项目->接口
    public ServerResponse addProject(String projectName, int projectId) throws Throwable {
        Project pj = new Project();
        ServerResponse sr = pj.addCargo(Integer.toString(projectId), pj.projectPath);
        if(sr.isSuccess()) {
            TreeNode node = new TreeNode(projectName, projectId);
            node.repoPath = pj.projectPath + Integer.toString(projectId);
            node.parent = root;
            root.childList.push(node);
        }
        return sr;
    }

    //添加任务->接口
    public ServerResponse addTask(String taskName, int taskId, int projectId) throws Throwable {
        Task ta = new Task();
        TreeNode parent = TreeNode.findNodeById(root, projectId);
        if(parent == null) {
            return ServerResponse.createByErrorMessage("project id not found!");
        }
        ServerResponse sr = ta.addCargo(Integer.toString(taskId),
                parent.repoPath + "/");//这里的cargo是root端
        if(sr.isSuccess()) {
            TreeNode node = new TreeNode(taskName, taskId);
            node.repoPath = ta.taskPath + Integer.toString(taskId);//这里的repo是master端
            node.parent = parent;
            parent.childList.push(node);
        }
        return sr;
    }

    //目前不支持删除操作
    //public int deleteProject(int projectHash) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectHash);
    //    pj.deleteCargo(node.repoPath);
    //    node.childList.clear();
    //    root.childList.remove(node);
    //    return 1;
    //}

    //public int deleteTask(int taskHash) throws Throwable {
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

    //fork任务->接口
    public ServerResponse<ForkInfo> forkTask(String userIdentify, int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = TreeNode.findNodeById(root, taskId);
        if(node == null) {
            return ServerResponse.createByErrorMessage(
                    "Unable to fork, task id not found");
        }
        return ta.forkCargo(userIdentify, node.repoPath);
    }


    //目前不支持项目的getCommitChannel
    //public ServerResponse<List<CommitInfo>> getProjectCommitChannel(int projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    return pj.getCommitChannel(node.repoPath, timeProgress);
    //}

    //获取任务各分支最新更改->接口
    public ServerResponse<List<CommitInfo>> getTaskCommitChannel(int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = TreeNode.findNodeById(root, taskId);
        if(node == null) {
            return ServerResponse.createByErrorMessage(
                    "Unable to get commit channel, task id not found");
        }
        return ta.getCommitChannel(node.repoPath);
    }

    //目前不支持项目的lookChange
    //public ServerResponse<String> lookProjectFile(String fileName, int commitHash, int projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    return pj.lookFile(fileName, commitHash, node.repoPath);
    //}

    //获取任务某分支commit某文件->接口
    public ServerResponse<ChangeInfo> checkTaskChange(String fileName, int commitHash, int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = TreeNode.findNodeById(root, taskId);
        if(node == null) {
            return ServerResponse.createByErrorMessage(
                    "Unable to check task change, task id not found");
        }
        return ta.checkChange(fileName, commitHash, node.repoPath);
    }

    //目前不支持项目的merge
    //public ServerResponse<Integer> processProjectCommit(int commitHash, int projectId) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectId);
    //    ServerResponse<Integer> sr = pj.mergeCargo(commitHash, node.repoPath);
    //    return sr;
    //}

    //任务通过commit合并分支
    public ServerResponse processTaskCommit(int commitHash, int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = TreeNode.findNodeById(root, taskId);
        if(node == null) {
            return ServerResponse.createByErrorMessage(
                    "Unable to process commit, task id not found");
        }
        return ta.mergeCargoByCommit(commitHash, node.repoPath);
    }

    //目前不支持任务向项目推送
    //Task向Project推送->接口，只有Task才有，和上一个都是merge过程，返回值应该是boolean
    //public void pushTask(int taskId) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = findNode(taskId);
    //    ta.pushTask(node.repoPath, node.parent.repoPath + "/" + node.name);
    //}

}