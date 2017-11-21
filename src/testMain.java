import org.eclipse.jgit.api.Git;

import java.io.File;
import java.util.Scanner;

/**
 * Created by jihang on 2017/11/9.
 */

class test {

    public static void main(String[] args) throws Throwable {

        String project = "p1";
        String task1 = "t1";
        String user1Iden = "user1";
        String user2Iden = "user2";
        String user1Dir = "mySpace/user1/";
        String user2Dir = "mySpace/user2/";

        int projectHash, taskHash;

        //添加项目和任务示例
        ReqProcess rp = new ReqProcess();
        projectHash = rp.addProject(project);
        taskHash = rp.addTask(task1, projectHash);
        rp.cargoInfo();//打印结构树

        //fork任务示例
        //1号用户
        ReturnType.forkInfo fi1 = rp.forkTask(user1Iden, taskHash);
        Git git1 = Git.cloneRepository()
                .setBranch(fi1.branchName)
                .setURI(fi1.repoUri)
                .setDirectory(new File(user1Dir))
                .call();
        //2号用户
        ReturnType.forkInfo fi2 = rp.forkTask(user2Iden, taskHash);
        Git git2 = Git.cloneRepository()
                .setBranch(fi2.branchName)
                .setURI(fi2.repoUri)
                .setDirectory(new File(user2Dir))
                .call();

        //修改过程
        Scanner input = new Scanner(System.in);
        input.next();

        //提交过程
        git1.add().addFilepattern(".").call();
        git1.commit().setCommitter(user1Iden, "email")
                .setMessage("user1 processed").call();
        git1.push().call();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Iden, "email")
                .setMessage("user2 processed").call();
        git2.push().call();

        //合并过程
        rp.mergeTask(taskHash);
        //rp.watchTask(taskHash);//打印主分支现状
    }
}