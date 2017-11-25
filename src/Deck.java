import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.lib.IndexDiff.StageState;
import org.eclipse.jgit.merge.Merger;
import org.eclipse.jgit.notes.Note;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.merge.Merger;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jihang on 2017/11/21.
 */

abstract class Deck {

    String projectPath = "mySpace/project/";
    String taskPath = "mySpace/task/";

    //在指定cargoPath建立名字为name的Git库
    abstract int addCargo(String name, String cargoPath) throws Throwable ;

    //删除指定cargoPath的Git库
    abstract int deleteCargo(String cargoPath) throws Throwable;

    //为指定userIden生成指定cargoPath的clone路径及分支名
    ReturnType.forkInfo forkCargo(String userIden, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        //存在则删除重建
        String branch = userIden + "@" + cargoPath;
        if(git.getRepository().findRef(branch) != null)
            git.branchDelete().setBranchNames(branch).call();
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

    //
    void revisionInfo(String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        RevWalk walk = new RevWalk(git.getRepository());
        git.checkout().setName("master").call();
        Ref refMaster = git.getRepository().findRef("master");
        RevCommit commitMaster = walk.parseCommit(refMaster.getObjectId());
        Iterable<RevCommit> commits = git.log().all().call();
        for (RevCommit commit : commits)
            System.out.println("LogCommit: " + commit.getFullMessage());
    }

    //指定cargoPath的Git库master分支合并
    //任务的mergeCargo仅适用于root库，master库的merge在任务类里写了
    void mergeCargo(String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        git.checkout().setName("master").call();
        RevWalk walk = new RevWalk(git.getRepository());

        List<Ref> refs = git.branchList().call();
        Ref ref0 = git.getRepository().findRef("master");
        RevCommit commit0 = walk.parseCommit(ref0.getObjectId());
        for(Ref ref : refs) {
            RevCommit commit = walk.parseCommit(ref.getObjectId());

            System.out.println(commit);
            statusCargo(git);
            git.merge().include(commit).call();
            statusCargo(git);

        }

    }

    //查看指定cargoPath的commit log
    void watchCargo(String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        git.checkout().setName("master").call();
        LogCommand log = git.log();
        Iterable<RevCommit> logMsgs = log.call();
        int count = 1;
        for (RevCommit commit : logMsgs) {
            System.out.println(count++ + ".");
            System.out.println(commit);
            System.out.println("committer :  " + commit.getAuthorIdent().getName());
            System.out.println("content  :  " + commit.getFullMessage());
            System.out.println("");
        }
    }

    private void statusCargo(Git git) throws Throwable {
        Status status = git.status().call();
        Set<String> conflicting = status.getConflicting();
        for(String conflict : conflicting) {
            System.out.println("Conflicting: " + conflict);
        }

        Set<String> added = status.getAdded();
        for(String add : added) {
            System.out.println("Added: " + add);
        }

        Set<String> changed = status.getChanged();
        for(String change : changed) {
            System.out.println("Change: " + change);
        }

        Set<String> missing = status.getMissing();
        for(String miss : missing) {
            System.out.println("Missing: " + miss);
        }

        Set<String> modified = status.getModified();
        for(String modify : modified) {
            System.out.println("Modification: " + modify);
        }

        Set<String> removed = status.getRemoved();
        for(String remove : removed) {
            System.out.println("Removed: " + remove);
        }

        Set<String> uncommittedChanges = status.getUncommittedChanges();
        for(String uncommitted : uncommittedChanges) {
            System.out.println("Uncommitted: " + uncommitted);
        }

        Set<String> untracked = status.getUntracked();
        for(String untrack : untracked) {
            System.out.println("Untracked: " + untrack);
        }

        Set<String> untrackedFolders = status.getUntrackedFolders();
        for(String untrack : untrackedFolders) {
            System.out.println("Untracked Folder: " + untrack);
        }

        Map<String, StageState> conflictingStageState = status.getConflictingStageState();
        for(Map.Entry<String, StageState> entry : conflictingStageState.entrySet()) {
            System.out.println("ConflictingState: " + entry);
        }
    }

}