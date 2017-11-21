
/**
 * Created by jihang on 2017/11/20.
 */

public class ReqProcess {

    TreeNode root = new TreeNode("root");

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
        node.repoPath = ta.taskPath + taskName;
        TreeNode parent = findNode(projectHash);
        node.parent = parent;
        parent.childlist.push(node);
        node.hashCode = ta.addCargo(taskName, parent.repoPath + "/");
        return node.hashCode;
    }

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

    TreeNode findNode(int nodeHash) {
        for(TreeNode tn : root.childlist) {
            if(tn.hashCode == nodeHash)
                return tn;
            for(TreeNode tnn : tn.childlist)
                if(tnn.hashCode == nodeHash)
                    return tnn;
        }
        return null;
    }

    void watchTask(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ta.watchCargo(node.repoPath);
    }

    void cargoInfo() {
        for(TreeNode tn : root.childlist) {
            System.out.println("Porject " + tn.name + " has tasks : ");
            for(TreeNode tnn : tn.childlist)
                System.out.println(tnn.name);
        }
    }

    void mergeTask(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ta.mergeTask(node.repoPath);
        ta.mergeCargo(node.parent.repoPath + "/" + node.name);
    }
}