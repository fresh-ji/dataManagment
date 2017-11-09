/**
 * Created by jihang on 2017/11/9.
 */

//package dataManagment;

import java.io.File;
import java.io.FileWriter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;


public class dmGit {

    public static void main(String[] args) throws Throwable{

        File root = new File("gitme");
        //建文件夹
        if(!root.exists())
            root.mkdir();
        //建文件,git init

        File gitF = new File("gitme/.git");
        if(!gitF.exists())
            Git.init().setDirectory(root).call();

        //打开库
        Git git = Git.open(root);
        //内容
        File newFile = new File("gitme"+System.currentTimeMillis()+".java");
        FileWriter fw = new FileWriter(newFile);
        fw.write("hello world!");
        fw.flush();
        fw.close();
        //添加,git add
        git.add().addFilepattern(".+").call();
        //提交,git commit
        git.commit().setCommitter("jihang", "jihang@me.com").setMessage("Try").call();
        //查看log
        for(RevCommit revCommit : git.log().call()) {
            System.out.println(revCommit);
            System.out.println(revCommit.getFullMessage());
            System.out.println(revCommit.getCommitterIdent().getName()+" "+revCommit.getCommitterIdent().getEmailAddress());

        }

    }
}
