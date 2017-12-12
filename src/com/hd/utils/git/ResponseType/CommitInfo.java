package com.hd.utils.git.ResponseType;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Created by jihang on 2017/12/12.
 */

public class CommitInfo {
    //作为getCommitChannel返回时使用
    public RevCommit commit;
    public String[] differs;
    public int order() {
        return commit.getCommitTime();
    }
}