import java.util.LinkedList;

/**
 * Created by jihang on 2017/11/20.
 */

class TreeNode {
    String name;
    String repoPath;
    TreeNode parent;
    int hashCode;
    LinkedList<TreeNode> childlist;
    TreeNode(String name) {
        this.name = name;
        repoPath = null;
        parent = null;
        childlist = new LinkedList<>();
    }
}