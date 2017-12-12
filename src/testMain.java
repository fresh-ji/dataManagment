import com.hd.utils.git.RequProcess;
import com.hd.utils.git.ResponseType.CommitInfo;
import com.hd.utils.git.ResponseType.ForkInfo;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

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

        /*
        //添加项目和任务示例
        RequProcess rp = RequProcess.getInstance();
        rp.addProject(project, 123);
        taskHash = rp.addTask(task1, projectHash);
        //rp.cargoInfo();//打印结构树

        //fork任务示例
        //1号用户
        ForkInfo fi1 = rp.forkTask(user1Ident, taskHash);
        Git git1 = Git.cloneRepository()
                .setBranch(fi1.branchName)
                .setURI(fi1.repoUri)
                .setDirectory(new File(user1Dir))
                .call();
        //2号用户
        ForkInfo fi2 = rp.forkTask(user2Ident, taskHash);
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
        //1号，两个文件提交一次
        File f1 = new File(user1Dir + "a.txt");
        FileWriter fw1 = new FileWriter(f1);
        fw1.write("aaaaad");
        fw1.flush();
        f1 = new File(user1Dir + "aa.txt");
        fw1 = new FileWriter(f1);
        fw1.write("aaaaac");
        fw1.flush();
        fw1.close();
        git1.add().addFilepattern(".").call();
        git1.commit().setCommitter(user1Ident, "email")
                .setMessage("user1 processed").call();
        git1.push().call();

        //2号，2个文件提交两次
        File f2 = new File(user2Dir + "b.txt");
        FileWriter fw2 = new FileWriter(f2);
        fw2.write("bbbbbd");
        fw2.flush();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Ident, "email")
                .setMessage("user2 processed first time").call();
        git2.push().call();
        f2 = new File(user2Dir + "bb.txt");
        fw2 = new FileWriter(f2);
        fw2.write("bbbbbc");
        fw2.flush();
        fw2.close();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Ident, "email")
                .setMessage("user2 processed second time").call();
        git2.push().call();

        //获取commit通道,相当于概览
        List<CommitInfo> channel = rp.getTaskCommitChannel(taskHash);

        /*
        for (ReturnType rt : channel) {
            ReturnType.commitInfo ci = rt.commitinfo;
            System.out.println("用户：" + ci.commit.getCommitterIdent().getName());
            System.out.println("名称：" + ci.commit.getFullMessage());
            System.out.println("时间：" + ci.commit.getCommitTime());
            System.out.println("哈希：" + ci.commit.hashCode());
            System.out.println("内容数量：" + ci.differs.length);
            for(String strr : ci.differs)
                System.out.println(strr);
        }
        */

        /*
        //处理和合并commit
        CommitInfo ci = channel.get(1);
        //String str = rp.lookCommitFile("aa.txt", ci.commit.hashCode(), taskHash);
        //System.out.println(str);
        rp.processTaskCommit(ci.commit.hashCode(), taskHash);//1号合并
        ci = channel.get(2);
        rp.processTaskCommit(ci.commit.hashCode(), taskHash);//2号合并

        //Task提交并合并root，文件加入到root后就到了Project文件系统中
        rp.pushTask(taskHash);
        */
    }
}