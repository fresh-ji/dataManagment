package com.hd.utils.git.Pojo;

import com.hd.utils.git.ResponseType.ChangeInfo;
import com.hd.utils.git.ResponseType.CommitInfo;
import com.hd.utils.git.ResponseType.ForkInfo;
import com.hd.utils.git.Common.ServerResponse;

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

import java.io.*;
import java.util.*;

/**
 * Created by jihang on 2017/11/21.
 */

abstract class Deck {

    public final String projectPath = "F:/mySpace/project/";
    public final String taskPath = "F:/mySpace/task/";

    /**
     * 建立名字为name的Git库，路径为cargoPath
     * @param name 库名
     * @param cargoPath 文件系统路径，不包括name，对于task来说是root
     */
    abstract ServerResponse addCargo(String name, String cargoPath) throws Throwable;

    //删除指定cargoPath的Git库
    //abstract int deleteCargo(String cargoPath) throws Throwable;

    /**
     * @param userIdentify 用户名
     * @param cargoPath 库路径，与add不同，这是全路径
     * @return clone路径及分支名
     */
    public ServerResponse<ForkInfo> forkCargo(String userIdentify, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        String branch = userIdentify + "@" + cargoPath;
        branch = branch.replace(":", "");
        if(git.getRepository().findRef(branch) != null) {
            return ServerResponse.createByErrorMessage("branch already exists!");
        }
        git.checkout().setName("master").call();
        git.branchCreate().setName(branch).call();

        git.checkout().setName(branch).call();
        git.commit().setCommitter(userIdentify, "email")
                .setMessage("Hi, this is " + branch + "!").call();

        ForkInfo fi = new ForkInfo();
        fi.repoUri = git.getRepository().getDirectory().getCanonicalPath();
        fi.branchName = git.getRepository().getBranch();
        return ServerResponse.createBySuccess(fi);
    }

    /**
     * 按分支返回管道，每个分支仅返回最新
     * @param cargoPath 库路径
     * @return commit管道，最终按时间顺序排列
     */
    public ServerResponse<List<CommitInfo>> getCommitChannel(String cargoPath) throws Throwable {
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
            //填补commit
            ci.commit = walk.parseCommit(ref.getObjectId());;
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

    /**
     * 查看以上channel中的单个文件具体更改情况
     * @param fileName 文件名
     * @param commitHash commit
     * @param cargoPath 库名
     * @return 该commit中该文件内容
     */
    public ServerResponse<ChangeInfo> checkChange(String fileName, int commitHash, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        RevCommit commit = prepareCommit(git, commitHash);
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = new TreeWalk(git.getRepository());
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(fileName));
        if (!treeWalk.next()) {
            return ServerResponse.createByErrorMessage(fileName + " not found!");
        }

        ObjectId objectId = treeWalk.getObjectId(0);
        ObjectLoader loader = git.getRepository().open(objectId);
        OutputStream os = new ByteArrayOutputStream();
        loader.copyTo(os);

        ChangeInfo ci = new ChangeInfo();
        ci.newContent = os.toString();

        File file = new File(cargoPath + "/" + fileName);
        if(!file.exists()) {
            ci.oldContent = null;
        } else {
            InputStream is = new FileInputStream(file);
            byte b[] = new byte[(int)file.length()];
            is.read(b);
            is.close();
            ci.oldContent = new String(b);
        }
        return ServerResponse.createBySuccess(ci);
    }

    /**
     * Git库合并指定commit所属分支
     * @param commitHash commit
     * @param cargoPath 库名
     */
    public ServerResponse mergeCargoByCommit(int commitHash, String cargoPath) throws Throwable {
        Git git = Git.open(new File(cargoPath));
        RevCommit commit = prepareCommit(git, commitHash);
        git.merge().include(commit).call();
        return ServerResponse.createBySuccess();
    }

    /*
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
    */
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

}