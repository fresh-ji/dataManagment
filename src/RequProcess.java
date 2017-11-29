import java.util.List;

/**
 * Created by jihang on 2017/11/22.
 */

public class RequProcess {

    //工程树根
    private TreeNode root = new TreeNode("root");

    //commit处理时间进度
    private int timeProgress = 0;

    //增接口
    int addProject(String projectName) throws Throwable {
        Project pj = new Project();
        TreeNode node = new TreeNode(projectName);
        node.repoPath = pj.projectPath + projectName;
        node.parent = root;
        root.childlist.push(node);
        node.hashCode = pj.addCargo(projectName, pj.projectPath);
        return node.hashCode;
    }

    int addTask(String taskName, int projectHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = new TreeNode(taskName);
        node.repoPath = ta.taskPath + taskName;//这里的repo是master
        TreeNode parent = findNode(projectHash);
        node.parent = parent;
        parent.childlist.push(node);
        node.hashCode = ta.addCargo(taskName,
                parent.repoPath + "/");//这里的cargo是root
        return node.hashCode;
    }

    //删接口
    int deleteProject(int projectHash) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectHash);
        pj.deleteCargo(node.repoPath);
        node.childlist.clear();
        root.childlist.remove(node);
        return 1;
    }

    int deleteTask(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ta.deleteCargo(node.repoPath);//删除master端
        ta.deleteCargo(node.parent.repoPath
                + "/" + node.name);
        node.parent.childlist.remove(node);
        return 1;
    }

    //fork接口
    ReturnType.forkInfo forkProject(String userIden, int projectHash) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectHash);
        ReturnType.forkInfo fi = pj.forkCargo(userIden, node.repoPath);
        return fi;
    }

    ReturnType.forkInfo forkTask(String userIden, int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ReturnType.forkInfo fi = ta.forkCargo(userIden, node.repoPath);
        return fi;
    }

    //列举commit接口，返回的太复杂，可以精简
    List<ReturnType> getProjectCommitChannel(int projectHash) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectHash);
        return pj.getCommitChannel(node.repoPath, timeProgress);
    }

    List<ReturnType> getTaskCommitChannel(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        return ta.getCommitChannel(node.repoPath, timeProgress);
    }

    //查看commit中file接口
    String lookProjectFile(String fileName, int commitHash, int projectHash) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectHash);
        return pj.lookFile(fileName, commitHash, node.repoPath);
    }

    String lookTaskFile(String fileName, int commitHash, int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        return ta.lookFile(fileName, commitHash, node.repoPath);
    }

    //处理commit接口
    void processProjectCommit(int commitHash, int projectHash) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectHash);
        timeProgress = pj.mergeCargo(commitHash, node.repoPath);
    }

    void processTaskCommit(int commitHash, int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        timeProgress = ta.mergeCargo(commitHash, node.repoPath);
    }

    //只有Task才有的推送，和上一个都是merge过程，返回值应该是boolean
    void pushTask(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ta.pushTask(node.repoPath, node.parent.repoPath + "/" + node.name);
    }

    //展示工程树接口
    void cargoInfo() {
        System.out.println("Cargo structure is : ");
        for(TreeNode tn : root.childlist) {
            System.out.println("Porject " + tn.name + " has tasks : ");
            for(TreeNode tnn : tn.childlist)
                System.out.println(tnn.name);
        }
    }

    private TreeNode findNode(int nodeHash) {
        for(TreeNode tn : root.childlist) {
            if(tn.hashCode == nodeHash)
                return tn;
            for(TreeNode tnn : tn.childlist)
                if(tnn.hashCode == nodeHash)
                    return tnn;
        }
        return null;
    }

}