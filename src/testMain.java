import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Created by jihang on 2017/11/9.
 */

class test {

    public static void main(String[] args) throws Throwable {

        String project = "p1";
        String task1 = "t1";
        String user1Ident = "user1";
        String user2Ident = "user2";
        //String user3Ident = "user3";
        String user1Dir = "mySpace/user1/";
        String user2Dir = "mySpace/user2/";
        //String user3Dir = "mySpace/user3/";

        int projectHash, taskHash;

        //添加项目和任务示例
        RequProcess rp = new RequProcess();
        projectHash = rp.addProject(project);
        taskHash = rp.addTask(task1, projectHash);
        //rp.cargoInfo();//打印结构树

        //fork任务示例
        //1号用户
        ReturnType.forkInfo fi1 = rp.forkTask(user1Ident, taskHash);
        Git git1 = Git.cloneRepository()
                .setBranch(fi1.branchName)
                .setURI(fi1.repoUri)
                .setDirectory(new File(user1Dir))
                .call();
        //2号用户
        ReturnType.forkInfo fi2 = rp.forkTask(user2Ident, taskHash);
        Git git2 = Git.cloneRepository()
                .setBranch(fi2.branchName)
                .setURI(fi2.repoUri)
                .setDirectory(new File(user2Dir))
                .call();
        //3号用户
        //ReturnType.forkInfo fi3 = rp.forkTask(user3Ident, taskHash);
        //Git git3 = Git.cloneRepository()
        //        .setBranch(fi3.branchName)
        //        .setURI(fi3.repoUri)
        //        .setDirectory(new File(user3Dir))
        //        .call();

        //提交过程
        //1号
        File f1 = new File(user1Dir + "a.txt");
        FileWriter fw1 = new FileWriter(f1);
        fw1.write("aaaaa");
        fw1.flush();
        f1 = new File(user1Dir + "aa.txt");
        fw1 = new FileWriter(f1);
        fw1.write("aaaaa");
        fw1.flush();
        fw1.close();
        git1.add().addFilepattern(".").call();
        git1.commit().setCommitter(user1Ident, "email")
                .setMessage("user1 processed").call();
        git1.push().call();
        //2号
        File f2 = new File(user2Dir + "a.txt");
        FileWriter fw2 = new FileWriter(f2);
        fw2.write("bbbbb");
        fw2.flush();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Ident, "email")
                .setMessage("user2 processed first time").call();
        git2.push().call();
        f2 = new File(user2Dir + "b.txt");
        fw2 = new FileWriter(f2);
        fw2.write("bbbbb");
        fw2.flush();
        fw2.close();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Ident, "email")
                .setMessage("user2 processed second time").call();
        git2.push().call();

        //合并过程
        rp.mergeTask(taskHash);
        //rp.watchTask(taskHash);//打印主分支现状
    }
}