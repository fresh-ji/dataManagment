import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.util.List;

/**
 * Created by jihang on 2017/11/21.
 */

abstract class Deck {

    String projectPath = "mySpace/project/";
    String taskPath = "mySpace/task/";

    int addCargo(String name, String cargoPath) throws Throwable {
        //建文件夹
        String masterPath = cargoPath + name;
        File file = new File(masterPath);
        if (file.exists()) {
            System.out.println(name + " already exists!!");
            return 0;
        }
        //建库
        Git git = Git.init().setDirectory(file).call();
        //提交第一个commit
        git.commit().setCommitter("master", "email")
                .setMessage("Hi, this is " + name + "!").call();
        return git.hashCode();
    }

    int deleteCargo(String cargoPath) throws Throwable {
        System.out.println("This is abstract deleteCargo!");
        return 1;
    }

    ReturnType.forkInfo forkCargo(String userIden, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        String branch = userIden + "@" + cargoPath;
        git.checkout().setName("master").call();
        git.branchCreate().setName(branch).call();
        git.checkout().setName(branch).call();
        git.commit().setCommitter(userIden, "email")
                .setMessage("Hi, this is " + branch + "!").call();
        ReturnType rt = new ReturnType("forkInfo");
        rt.forkinfo.repoUri = git.getRepository().getDirectory().getCanonicalPath();
        rt.forkinfo.branchName = git.getRepository().getBranch();
        return rt.forkinfo;
    }

    void watchCargo(String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        git.checkout().setName("master").call();
        LogCommand log = git.log();
        Iterable<RevCommit> logMsgs = log.call();
        int count = 1;
        for (RevCommit commit : logMsgs) {
            System.out.println(count++ + ".");
            System.out.println(commit);
            System.out.println("commiter :  " + commit.getAuthorIdent().getName());
            System.out.println("content  :  " + commit.getFullMessage());
            System.out.println("");
        }
    }

    void mergeCargo(String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        git.checkout().setName("master").call();
        List<Ref> refs = git.branchList().call();
        for(Ref ref : refs)
            git.merge().include(ref).call();
    }
}