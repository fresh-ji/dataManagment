import java.util.Objects;

/**
 * Created by jihang on 2017/11/21.
 */

public class ReturnType {

    public class forkInfo {
        String repoUri;
        String branchName;
    }

    forkInfo forkinfo;

    ReturnType(String str) {
        if(Objects.equals(str, "forkInfo"))
            forkinfo = new forkInfo();
    }

}
