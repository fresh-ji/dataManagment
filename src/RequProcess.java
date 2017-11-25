
/**
 * Created by jihang on 2017/11/22.
 */

public class RequProcess {

    //工程树根
    TreeNode root = new TreeNode("root");

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

    //merge接口
    void mergeProject(int projectHash) throws Throwable {
        Project pj = new Project();
        TreeNode node = findNode(projectHash);
        pj.mergeCargo(node.repoPath);
    }

    void mergeTask(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        ta.revisionInfo(node.repoPath);//master合并并提交root
        //ta.mergeCargo(node.parent.repoPath + "/" + node.name);//root合并
    }

    //查commit接口
    void watchTask(int taskHash) throws Throwable {
        Task ta = new Task();
        TreeNode node = findNode(taskHash);
        System.out.println("Repository on master side : ");
        ta.watchCargo(node.repoPath);
        System.out.println("Repository on root side : ");
        ta.watchCargo(node.parent.repoPath + "/" + node.name);
    }

    //工程树处理接口
    void cargoInfo() {
        System.out.println("Cargo structure is : ");
        for(TreeNode tn : root.childlist) {
            System.out.println("Porject " + tn.name + " has tasks : ");
            for(TreeNode tnn : tn.childlist)
                System.out.println(tnn.name);
        }
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

}