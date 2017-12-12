package com.hd.utils.git;

import com.hd.utils.git.POJO.Project;
import com.hd.utils.git.POJO.Task;
import com.hd.utils.git.POJO.TreeNode;
import com.hd.utils.git.ResponseType.CommitInfo;
import com.hd.utils.git.ResponseType.ForkInfo;
import com.hd.utils.git.common.ServerResponse;

import java.util.List;

/**
 * Created by jihang on 2017/11/22.
 */

public class RequProcess {

    //工程树根
    private TreeNode root;

    //commit处理时间进度
    private int timeProgress;

    private static class lazyHolder {
        private static final RequProcess rq = new RequProcess();
    }
    private RequProcess() {
        root = new TreeNode("root");
        timeProgress = 0;
    }

    public static RequProcess getInstance() {
        return lazyHolder.rq;
    }

    //增->接口
    public ServerResponse addProject(String projectName, int projectId) throws Throwable {
        Project pj = new Project();
        ServerResponse sr = pj.addCargo(projectName, pj.projectPath);
        if(sr.isSuccess()) {
            TreeNode node = new TreeNode(projectName);
            node.repoPath = pj.projectPath + projectName;
            node.parent = root;
            root.childlist.push(node);
            node.idCode = projectId;
        }
        return sr;
    }

    public ServerResponse addTask(String taskName, int projectId, int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode parent = findNode(projectId);
        ServerResponse sr = ta.addCargo(taskName,
                parent.repoPath + "/");//这里的cargo是root
        if(sr.isSuccess()) {
            TreeNode node = new TreeNode(taskName);
            node.repoPath = ta.taskPath + taskName;//这里的repo是master
            node.parent = parent;
            parent.childlist.push(node);
            node.idCode = taskId;
        }
        return sr;
    }

    //删->接口
    //public int deleteProject(int projectHash) throws Throwable {
    //    Project pj = new Project();
    //    TreeNode node = findNode(projectHash);
    //    pj.deleteCargo(node.repoPath);
    //    node.childlist.clear();
    //    root.childlist.remove(node);
    //    return 1;
    //}

    //public int deleteTask(int taskHash) throws Throwable {
    //    Task ta = new Task();
    //    TreeNode node = findNode(taskHash);
    //    ta.deleteCargo(node.repoPath);//删除master端
    //    ta.deleteCargo(node.parent.repoPath
    //            + "/" + node.name);
    //    node.parent.childlist.remove(node);
    //    return 1;
    //}

    //fork->接口
    public ServerResponse<ForkInfo> forkProject(String userIden, int projectId) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectId);
        return pj.forkCargo(userIden, node.repoPath);
    }

    public ServerResponse<ForkInfo> forkTask(String userIden, int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskId);
        return ta.forkCargo(userIden, node.repoPath);
    }

    //列举commit->接口，返回的太复杂，可以精简
    public ServerResponse<List<CommitInfo>> getProjectCommitChannel(int projectId) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectId);
        return pj.getCommitChannel(node.repoPath, timeProgress);
    }

    public ServerResponse<List<CommitInfo>> getTaskCommitChannel(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        return ta.getCommitChannel(node.repoPath, timeProgress);
    }

    //查看commit中file->接口
    public ServerResponse<String> lookProjectFile(String fileName, int commitHash, int projectId) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectId);
        return pj.lookFile(fileName, commitHash, node.repoPath);
    }

    public ServerResponse<String> lookTaskFile(String fileName, int commitHash, int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskId);
        return ta.lookFile(fileName, commitHash, node.repoPath);
    }

    //处理commit->接口
    public ServerResponse<Integer> processProjectCommit(int commitHash, int projectId) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectId);
        ServerResponse<Integer> sr = pj.mergeCargo(commitHash, node.repoPath);
        return sr;
    }

    public ServerResponse<Integer> processTaskCommit(int commitHash, int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ServerResponse<Integer> sr = ta.mergeCargo(commitHash, node.repoPath);
        return sr;
    }

    //Task向Project推送->接口，只有Task才有，和上一个都是merge过程，返回值应该是boolean
    public void pushTask(int taskId) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskId);
        ta.pushTask(node.repoPath, node.parent.repoPath + "/" + node.name);
    }

    //展示工程树接口
    public void cargoInfo() {
        System.out.println("Cargo structure is : ");
        for(TreeNode tn : root.childlist) {
            System.out.println("Porject " + tn.name + " has tasks : ");
            for(TreeNode tnn : tn.childlist){
                System.out.println(tnn.name);
            }

        }
    }

    public TreeNode findNode(int nodeHash) {
        for(TreeNode tn : root.childlist) {
            if(tn.idCode == nodeHash){
                return tn;
            }

            for(TreeNode tnn : tn.childlist){
                if(tnn.idCode == nodeHash){
                    return tnn;
                }
            }

        }
        return null;
    }

}