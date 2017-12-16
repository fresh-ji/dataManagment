package com.hd.utils.git;

import com.hd.utils.git.common.ServerResponse;
import com.hd.utils.git.handle.RequestProcess;
import com.hd.utils.git.responsetype.ChangeInfo;
import com.hd.utils.git.responsetype.CommitInfo;
import com.hd.utils.git.responsetype.ForkInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by jihang on 2017/11/9.
 */

class Test {

    public static void main(String[] args) throws Throwable {

        String projectName = "p1";
        Long projectId = (long)1;
        String taskName = "t1";
        Long taskId = (long)11;
        String user1Identify = "xiaoming";
        String user2Identify = "xiaohong";
        String user1Dir = "F:/mySpace/user1/";
        String user2Dir = "F:/mySpace/user2/";

        //添加项目和任务示例
        ServerResponse sr1;
        sr1 = RequestProcess.addProject(projectName, projectId);
        if(!sr1.isSuccess()) {
            System.out.println(sr1.getMsg());
        }
        sr1 = RequestProcess.addTask(taskName, taskId, projectId);
        if(!sr1.isSuccess()) {
            System.out.println(sr1.getMsg());
        }

        //fork任务示例
        //1号用户
        ServerResponse<ForkInfo> sr2;
        sr2 = RequestProcess.forkTask(user1Identify, taskId);
        Git git1 = Git.cloneRepository()
                .setBranch(sr2.getData().branchName)
                .setURI(sr2.getData().repoUri)
                .setDirectory(new File(user1Dir))
                .call();
        //2号用户
        sr2 = RequestProcess.forkTask(user2Identify, taskId);
        Git git2 = Git.cloneRepository()
                .setBranch(sr2.getData().branchName)
                .setURI(sr2.getData().repoUri)
                .setDirectory(new File(user2Dir))
                .call();

        /*
        //提交过程
        //1号，两个文件提交一次
        File f1 = new File(user1Dir + "a.txt");
        FileWriter fw1 = new FileWriter(f1);
        fw1.write("aaaaaaa");
        fw1.flush();
        f1 = new File(user1Dir + "b.txt");
        fw1 = new FileWriter(f1);
        fw1.write("bbbbbbb");
        fw1.flush();
        fw1.close();
        git1.add().addFilepattern(".").call();
        git1.commit().setCommitter(user1Identify, "email")
                .setMessage("user1 add a and b!").call();
        git1.push().call();
        Thread.sleep(1000);

        //2号，2个文件提交两次
        File f2 = new File(user2Dir + "c.txt");
        FileWriter fw2 = new FileWriter(f2);
        fw2.write("ccccccc");
        fw2.flush();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Identify, "email")
                .setMessage("user2 add c!").call();
        git2.push().call();
        f2 = new File(user2Dir + "d.txt");
        fw2 = new FileWriter(f2);
        fw2.write("ddddddd");
        fw2.flush();
        fw2.close();
        git2.add().addFilepattern(".").call();
        git2.commit().setCommitter(user2Identify, "email")
                .setMessage("user2 add d!").call();
        git2.push().call();

        //获取commit通道,相当于概览
        ServerResponse<List<CommitInfo>> sr3;
        sr3 = rp.getTaskCommitChannel(taskId);
        if(!sr3.isSuccess()) {
            System.out.println(sr3.getMsg());
        }
        print(sr3.getData());

        RevCommit commit;
        /*
        //查看对于文件的更改
        ServerResponse<ChangeInfo> sr4;
        commit = sr3.getData().get(2).commit;
        sr4 = rp.checkTaskChange("c.xls", commit.hashCode(), taskId);
        if(!sr4.isSuccess()) {
            System.out.println(sr4.getMsg());
        }
        //System.out.println(sr4.getData());
        */

        //合并commit
        /*
        ServerResponse sr5;
        commit = sr3.getData().get(1).commit;
        //1号合并
        sr5 = rp.processTaskCommit(commit.hashCode(), taskId);
        if(!sr5.isSuccess()) {
            System.out.println(sr5.getMsg());
        }
        commit = sr3.getData().get(2).commit;
        //2号合并
        sr5 = rp.processTaskCommit(commit.hashCode(), taskId);
        if(!sr5.isSuccess()) {
            System.out.println(sr5.getMsg());
        }
        */

        //二次修改
        /*
        fw1 = new FileWriter(f1);
        fw1.write("eeeeeee");
        fw1.flush();
        fw1.close();
        git1.add().addFilepattern(".").call();
        git1.commit().setCommitter(user1Identify, "email")
                .setMessage("user1 modify b!").call();
        git1.push().call();

        sr3 = rp.getTaskCommitChannel(taskId);
        if(!sr3.isSuccess()) {
            System.out.println(sr3.getMsg());
        }
        //print(sr3.getData());
        */
        /*
        commit = sr3.getData().get(2).commit;
        sr4 = rp.checkTaskChange("b.xls",
                commit.hashCode(), taskId);
        if(!sr4.isSuccess()) {
            System.out.println(sr4.getMsg());
        }
        System.out.println(sr4.getData().oldContent);
        System.out.println(sr4.getData().newContent);
        */
    }

    private static void print(List<CommitInfo> data) {
        for (CommitInfo ci : data) {
            //System.out.println("用户：" + ci.commit.getCommitterIdent().getName());
            System.out.println("名称：" + ci.commit.getFullMessage());
            //System.out.println("时间：" + ci.commit.getCommitTime());
            //System.out.println("哈希：" + ci.commit.hashCode());
            //System.out.println("内容数量：" + ci.differs.length);
            for(String str2 : ci.differs) {
                System.out.println(str2);
            }
        }
        System.out.println("*****************************");
    }
}