package com.hd.utils.git.responsetype;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * 作为getCommitChannel返回时的管道元素
 * @author jihang
 * @date 2017/12/12
 */

public class CommitInfo {
    public RevCommit commit;
    public String[] differs;
    public Integer order() {
        return commit.getCommitTime();
    }
}