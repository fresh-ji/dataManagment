import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * Created by jihang on 2017/11/20.
 */

public class Task extends Deck {

    @Override
    int addCargo(String name, String cargoPath) throws Throwable {
        //建master文件夹
        String masterPath = cargoPath + name;
        File masetrFile = new File(masterPath);
        if (masetrFile.exists()) {
            System.out.println(name + " already exists on master side!!");
            return 0;
        }
        //建库
        Git masterGit = Git.init().setDirectory(masetrFile).call();
        //提交第一个commit
        masterGit.commit().setCommitter("master", "email")
                .setMessage("Hi, this is " + name + " on master side!")
                .call();
        //建slave文件夹
        String slavePath = taskPath + name;
        File salveFile = new File(slavePath);
        if (salveFile.exists()) {
            System.out.println(name + " already exists on slave side!!");
            return 0;
        }
        //克隆库
        String uri = masterGit.getRepository().getDirectory()
                .getCanonicalPath();
        Git slaveGit = Git.cloneRepository().setURI(uri)
                .setDirectory(salveFile).call();
        //提交第一个commit
        slaveGit.commit().setCommitter("slave", "email")
                .setMessage("Hi, this is " + name + " on slave side!")
                .call();
        return slaveGit.hashCode();
    }

    void mergeTask(String taskPath) throws Throwable {
        super.mergeCargo(taskPath);
        Git git = Git.open(new File(taskPath));
        git.checkout().setName("master").call();
        git.push().call();
    }
}
