package com.hd.utils.git.ResponseType;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Created by jihang on 2017/12/12.
 */

//作为getCommitChannel返回时的管道元素
public class CommitInfo {
    public RevCommit commit;
    public String[] differs;
    public int order() {
        return commit.getCommitTime();
    }
}