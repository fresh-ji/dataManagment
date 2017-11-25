import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * Created by jihang on 2017/11/21.
 */

public class Project extends Deck {

    @Override
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

    @Override
    int deleteCargo(String cargoPath) throws Throwable {
        System.out.println("here delete project!");
        return 0;
    }

}
