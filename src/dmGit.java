/**
 * Created by jihang on 2017/11/9.
 */

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class dmGit {

    HashMap<Integer, String> projectMap = new HashMap();
    HashMap<Integer, String> taskMap = new HashMap();
    HashMap<Integer, String> subtaskMap = new HashMap();

    String masterRoot = "mySpace/master/";
    String projectRoot = "mySpace/projects/";
    String taskRoot = "mySpace/tasks/";
    String subtaskRoot = "mySpace/subtasks/";

    int addProject(String projectName) throws Throwable {
        //主分支
        String masterPath = masterRoot + projectName;
        File masterFile = new File(masterPath);
        //建文件夹
        if(!masterFile.exists()) {
            masterFile.mkdir();
        } else {  //项目名称重复
            System.out.println("project already exists!!");
            return 0;
        }
        //建库
        Git masterGit = Git.init().setDirectory(masterFile).call();

        //写README.md
        //File Info = new File(masterPath+"/README.md");
        //FileWriter fw = new FileWriter(Info);
        //fw.write(projectName);
        //fw.flush();
        //fw.close();
        //masterGit.add().addFilepattern("/README.md").call();
        //提交第一个commit
        masterGit.commit().setMessage("Hello World!").call();
        masterGit.branchCreate().setName("master2").call();

        List<Ref> refs = masterGit.branchList().call();
        for(Ref ref : refs)
            System.out.println(ref.toString());

        //从分支
        String projectPath = projectRoot + projectName;
        File projectFile = new File(projectPath);
        //Repository repo = masterGit.getRepository();
        //masterGit.branchCreate().setName("a").call();
        Git.cloneRepository().setURI("https://github.com/fresh-ji/myFirstRep.git").setDirectory(projectFile).setBranch("SLAVE").call();
        //Git.cloneRepository().setURI(masterPath).setDirectory(projectFile).call();

        Git slaveGit =Git.open(projectFile);
        slaveGit.commit().setMessage("slave hello world").call();
        //System.out.println(slaveGit.branchList().toString());

        //建文件夹
        //if(!projectFile.exists()) {
        //    projectFile.mkdir();
        //} else {//项目名称重复
        //    System.out.println("project already exists!!");
        //    return 0;
        //}
        //建库
        //Git git = Git.init().setDirectory(projectFile).call();
        //更新map
        //projectMap.put(git.hashCode(), repoPath);
        //System.out.println("project " + git.hashCode() + " established!!");
        //写README.md
        //File projectInfo = new File(repoPath+"/README.md");
        //FileWriter fw = new FileWriter(projectInfo);
        //fw.write(projectName);
        //fw.flush();
        //fw.close();
        //Git git2 = Git.open(projectFile);
        //git2.add().addFilepattern("README.md").call();
        //git2.commit().setMessage("Create README.md").call();
        //git2.close();
        //返回
        //return git.hashCode();
        return 1;
    }

    int addTask(int projectHash, String taskName) throws Throwable {
        try {

            Git masterGit = Git.open(new File(masterRoot + "p1"));
            //masterGit.branchCreate().setName("a").call();

            //String rootPath = projectMap.get(projectHash);
            //打开库，对应RepositoryNotFoundException
            //Git git = Git.open(new File(rootPath));
            //先建文件夹还是先branch
            //git.branchCreate().setName(taskName).call();

            //Ref ref = git.getRepository().findRef(taskName);
            //git.checkout().setName(taskName).call();
            //System.out.println(git.getRepository().getBranch());

            //File taskFile = new File(rootPath+"/"+taskName);
            //if(!taskFile.exists()) {
            //    taskFile.mkdir();
            //} else { //任务名称重复
            //   System.out.println("task already exists!!");
            //   return 0;
            //}

            //File gitF = new File(taskFile+"/.git");

            //Git git2 = Git.init().setDirectory(taskFile).call();

            //更新map
            //taskMap.put(ref.hashCode(), rootPath+"/"+taskName);
            //System.out.println("task " + ref.hashCode() + " established!!");
            //写README.md
            //File taskInfo = new File(rootPath+"/"+taskName+"/README.md");
            //FileWriter fw = new FileWriter(taskInfo);
            //fw.write(taskName);
            //fw.flush();
            //fw.close();
            //git2.add().addFilepattern("README.md").call();
            //git2.commit().setMessage("Create README.md").call();
            //git2.close();
            //git.add().addFilepattern(rootPath+"/"+taskName).call();
            //git.add().addFilepattern(rootPath+"/"+taskName+"/README.md").call();
            //git.commit().setMessage("add task").call();

            //System.out.println(git.getRepository().readCommitEditMsg().toString());

            //List<DiffEntry> diffEntries = git.diff().call();
            //for(DiffEntry df : diffEntries)
            //    System.out.println(df.toString());

            //for(String str : git.getRepository().getWorkTree().list())
            //    System.out.println(str);
            return 1;
        } catch (RepositoryNotFoundException e) {
            System.out.println("project not found!!");
            return 0;
        }
    }
}

/*




        //添加,git add
        git.add().addFilepattern(".+").call();
        //提交,git commit
        git.commit().setCommitter("jihang", "jihang@me.com").setMessage("Try").call();
        //查看log
        for(RevCommit revCommit : git.log().call()) {
        System.out.println(revCommit);
        System.out.println(revCommit.getFullMessage());
        System.out.println(revCommit.getCommitterIdent().getName()+" "+revCommit.getCommitterIdent().getEmailAddress());
//建info
        //File projectInfo = new File(rootPath+"/info.txt");
        //FileWriter fw = new FileWriter(projectInfo);
        //fw.write("hello world!!\nThis is project "+projectIndex);
        //fw.flush();
        //fw.close();
        //添加info
        //Git git = Git.open(projectFile);
        //git.add().addFilepattern("info.txt").call();
        //RevCommit commit = git.commit().setMessage("Create Project and Info").call();
        //git.close();
        }
        */