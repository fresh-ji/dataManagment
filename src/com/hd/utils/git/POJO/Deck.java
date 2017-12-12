package com.hd.utils.git.POJO;

import com.hd.utils.git.ResponseType.CommitInfo;
import com.hd.utils.git.ResponseType.ForkInfo;
import com.hd.utils.git.common.ServerResponse;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.lib.IndexDiff.StageState;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by jihang on 2017/11/21.
 */

abstract class Deck {

    public String projectPath = "mySpace/project/";
    public String taskPath = "mySpace/task/";

    /**
     * 在指定cargoPath建立名字为name的Git库
     * @param name 库名
     * @param cargoPath 库路径
     */
    abstract ServerResponse addCargo(String name, String cargoPath) throws Throwable;

    //删除指定cargoPath的Git库
    //abstract int deleteCargo(String cargoPath) throws Throwable;

    /**
     * fork是在确实存在新改动的情况下调用，所以旧分支会被删除
     * @param userIden 用户名
     * @param cargoPath 库路径
     * @return clone路径及分支名
     */
    public ServerResponse<ForkInfo> forkCargo(String userIden, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        //存在则删除重建
        String branch = userIden + "@" + cargoPath;
        if(git.getRepository().findRef(branch) != null) {
            git.branchDelete().setBranchNames(branch).call();
        }
        git.checkout().setName("master").call();
        git.branchCreate().setName(branch).call();

        git.checkout().setName(branch).call();
        git.commit().setCommitter(userIden, "email")
                .setMessage("Hi, this is " + branch + "!").call();
        ForkInfo fi = new ForkInfo();
        fi.repoUri = git.getRepository().getDirectory().getCanonicalPath();
        fi.branchName = git.getRepository().getBranch();
        return ServerResponse.createBySuccess(fi);
    }

    /**
     * 按时间顺序返回管道，是基于分支的，Project无分支时用不到
     * @param cargoPath 库路径
     * @param time
     * @return commit管道
     */
    public ServerResponse<List<CommitInfo>> getCommitChannel(String cargoPath, int time) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        git.checkout().setName("master").call();
        RevWalk walk = new RevWalk(git.getRepository());

        List<Ref> refs = git.branchList().call();
        ArrayList<CommitInfo> channel = new ArrayList<>();
        for(Ref ref : refs) {
            //跳过master分支
            //if(Objects.equals("refs/heads/master", ref.getName()))
            //    continue;
            CommitInfo ci = new CommitInfo();
            //填补commit，对于处理过的就不要了
            RevCommit commit = walk.parseCommit(ref.getObjectId());

            if(commit.getCommitTime() <= time){
                continue;
            }

            ci.commit = commit;
            //填补differs
            final List<DiffEntry> diffs = git.diff()
                    .setOldTree(prepareTreeParser(git, "master"))
                    .setNewTree(prepareTreeParser(git, ref.getName()))
                    .call();
            ci.differs = new String[diffs.size()];
            for(int i=0;i<diffs.size();++i) {
                DiffEntry diff = diffs.get(i);
                ci.differs[i] = diff.getChangeType() + ": "
                        + (diff.getOldPath().equals(diff.getNewPath())
                        ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath());
            }
            channel.add(ci);
        }
        channel.sort(Comparator.comparing(CommitInfo::order));
        walk.dispose();
        return ServerResponse.createBySuccess(channel);
    }

    //指定cargoPath的指定commit的file
    public ServerResponse<String> lookFile(String fileName, int commitHash, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        RevCommit commit = prepareCommit(git, commitHash);
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = new TreeWalk(git.getRepository());
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(fileName));
        if (!treeWalk.next()) {
            return null;
        }

        ObjectId objectId = treeWalk.getObjectId(0);
        ObjectLoader loader = git.getRepository().open(objectId);
        OutputStream os = new ByteArrayOutputStream();
        loader.copyTo(os);
        return ServerResponse.createBySuccess(os.toString());
    }

    //指定cargoPath的Git库master分支合并指定commit
    public ServerResponse<Integer> mergeCargo(int commitHash, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        RevCommit commit = prepareCommit(git, commitHash);
        git.merge().include(commit).call();
        return ServerResponse.createBySuccess(commit.getCommitTime());
    }

    //查看库状态
    private void statusCargo(Git git) throws Throwable {
        Status status = git.status().call();
        Set<String> conflicting = status.getConflicting();
        for(String conflict : conflicting) {
            System.out.println("Conflicting: " + conflict);
        }

        Map<String, StageState> conflictingStageState = status.getConflictingStageState();
        for(Map.Entry<String, StageState> entry : conflictingStageState.entrySet()) {
            System.out.println("ConflictingState: " + entry);
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
    }

    //根据ref建立树
    private AbstractTreeIterator prepareTreeParser(Git git, String ref) throws Throwable {
        Repository repository = git.getRepository();
        RevWalk walk = new RevWalk(repository);
        Ref REF = repository.findRef(ref);
        RevCommit commit = walk.parseCommit(REF.getObjectId());
        //把commit walk成树
        RevTree tree = walk.parseTree(commit.getTree().getId());
        CanonicalTreeParser treeParser = new CanonicalTreeParser();
        ObjectReader reader = repository.newObjectReader();
        treeParser.reset(reader, tree.getId());
        walk.dispose();
        return treeParser;
    }

    //根据hash找commit
    private RevCommit prepareCommit(Git git, int commitHash) throws Throwable {
        git.checkout().setName("master").call();
        RevWalk walk = new RevWalk(git.getRepository());

        List<Ref> refs = git.branchList().call();
        Ref ref0 = git.getRepository().findRef("master");
        for(Ref ref : refs) {
            RevCommit commit = walk.parseCommit(ref.getObjectId());
            if(commit.hashCode() == commitHash){
                ref0 = ref;
            }
        }
        return walk.parseCommit(ref0.getObjectId());
    }

}