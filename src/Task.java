import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import java.io.File;

/**
 * Created by jihang on 2017/11/21.
 */

public class Task extends Deck {

    @Override
    //两个库，cargoPath指的是root库路径
    int addCargo(String name, String cargoPath) throws Throwable {
        //建root文件夹
        String rootPath = cargoPath + name;
        File rootFile = new File(rootPath);
        if (rootFile.exists()) {
            System.out.println(name + " already exists on root side!!");
            return 0;
        }
        //建库
        Git rootGit = Git.init().setDirectory(rootFile).call();
        //提交第一个commit
        rootGit.commit().setCommitter("root", "email")
                .setMessage("Hi, this is " + name + " on root side!")
                .call();
        //建slave文件夹
        String masterPath = taskPath + name;
        File masterFile = new File(masterPath);
        if (masterFile.exists()) {
            System.out.println(name + " already exists on master side!!");
            return 0;
        }
        //克隆库
        String uri = rootGit.getRepository().getDirectory()
                .getCanonicalPath();
        Git masterGit = Git.cloneRepository().setURI(uri)
                .setDirectory(masterFile).call();
        //提交第一个commit
        masterGit.commit().setCommitter("master", "email")
                .setMessage("Hi, this is " + name + " on master side!")
                .call();
        return masterGit.hashCode();
    }

    @Override
    int deleteCargo(String cargoPath) throws Throwable {
        System.out.println("here delete task");
        return 0;
    }

    void pushTask(String taskPath, String rootPath) throws Throwable {
        Git git = Git.open(new File(taskPath));
        git.checkout().setName("master").call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage(git.toString() + " commit!!");
        git.push().call();

        Git rootgit = Git.open(new File(rootPath));
        rootgit.checkout().setName("master").call();
        Ref ref = rootgit.getRepository().findRef("master");
        rootgit.merge().include(ref).call();
    }
}
