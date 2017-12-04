import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Objects;

/**
 * Created by jihang on 2017/11/21.
 */

public class ReturnType {

    //作为forkCargo返回时使用
    class forkInfo {
        String repoUri;
        String branchName;
    }
    forkInfo forkinfo;

    //作为getCommitChannel返回时使用
    class commitInfo {
        RevCommit commit;
        String[] differs;
    }
    commitInfo commitinfo;
    int order() {
        return commitinfo.commit.getCommitTime();
    }

    //构造时实例化相应类
    ReturnType(String str) {
        if(Objects.equals(str, "forkInfo")) {
            forkinfo = new forkInfo();
        }
        if(Objects.equals(str, "commitInfo")) {
            commitinfo = new commitInfo();
        }
    }



}
