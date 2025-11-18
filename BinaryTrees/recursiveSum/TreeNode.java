

 public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
 }

 class Solution {
    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private boolean isValidBST(TreeNode root, long min, long max) {
        // Base case: empty subtree is valid
        if (root == null) {
            return true;  // Leaf's children return true here
        }
        
        // Check if current node violates BST property
        if (root.val <= min || root.val >= max) {
            return false;
        }
        
        // Recursively check left and right subtrees with updated bounds
        return isValidBST(root.left, min, root.val) && 
               isValidBST(root.right, root.val, max);
    }
}

class TestBST {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1: Valid BST
        //       5
        //      / \
        //     3   7
        //    / \
        //   2   4
        TreeNode node2 = new TreeNode(2);
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3, node2, node4);
        TreeNode node7 = new TreeNode(7);
        TreeNode root1 = new TreeNode(5, node3, node7);
        
        System.out.println("Test 1 - Valid BST: " + solution.isValidBST(root1)); // Expected: true
        
        // Test Case 2: Invalid BST (6 is greater than root 5)
        //       5
        //      /
        //     3
        //    / \
        //   2   6  ← Invalid: 6 > 5
        TreeNode node2b = new TreeNode(2);
        TreeNode node6 = new TreeNode(6);
        TreeNode node3b = new TreeNode(3, node2b, node6);
        TreeNode root2 = new TreeNode(5, node3b, null);
        
        System.out.println("Test 2 - Invalid BST: " + solution.isValidBST(root2)); // Expected: false
        
        // Test Case 3: Single node (valid BST)
        TreeNode root3 = new TreeNode(1);
        System.out.println("Test 3 - Single node: " + solution.isValidBST(root3)); // Expected: true
        
        // Test Case 4: Empty tree (null root)
        System.out.println("Test 4 - Empty tree: " + solution.isValidBST(null)); // Expected: true
        
        // Test Case 5: Invalid BST (duplicate values)
        //       2
        //      / \
        //     2   2
        TreeNode node2c = new TreeNode(2);
        TreeNode node2d = new TreeNode(2);
        TreeNode root5 = new TreeNode(2, node2c, node2d);
        
        System.out.println("Test 5 - Duplicate values: " + solution.isValidBST(root5)); // Expected: false
        
        // Test Case 6: Valid BST with negative numbers
        //      -10
        //      /  \
        //    -20   0
        TreeNode nodeNeg20 = new TreeNode(-20);
        TreeNode node0 = new TreeNode(0);
        TreeNode root6 = new TreeNode(-10, nodeNeg20, node0);
        
        System.out.println("Test 6 - Negative numbers: " + solution.isValidBST(root6)); // Expected: true
        
        // Test Case 7: Invalid BST (right child less than root)
        //       5
        //      / \
        //     3   4  ← Invalid: 4 < 5
        TreeNode node3c = new TreeNode(3);
        TreeNode node4b = new TreeNode(4);
        TreeNode root7 = new TreeNode(5, node3c, node4b);
        
        System.out.println("Test 7 - Right child < root: " + solution.isValidBST(root7)); // Expected: false
        
        // Test Case 8: Valid BST (larger tree)
        //        10
        //       /  \
        //      5    15
        //     / \     \
        //    3   7     20
        TreeNode node3d = new TreeNode(3);
        TreeNode node7b = new TreeNode(7);
        TreeNode node5 = new TreeNode(5, node3d, node7b);
        TreeNode node20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15, null, node20);
        TreeNode root8 = new TreeNode(10, node5, node15);
        
        System.out.println("Test 8 - Larger valid BST: " + solution.isValidBST(root8)); // Expected: true
    }
}