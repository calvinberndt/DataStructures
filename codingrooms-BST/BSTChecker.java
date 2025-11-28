import java.util.*;

public class BSTChecker {
   public static BSTNode checkBSTValidity(BSTNode rootNode) {
      // If the tree is empty, it's valid
      if (rootNode == null) {
         return null;
      }
      
      // We need to keep track of which nodes we've already visited
      // This helps us find nodes that are children of multiple parents
      Set<BSTNode> visitedNodes = new HashSet<>();
      
      // Start checking from the root
      // At the root, there are no restrictions on the key value
      // So we pass null for both minKey and maxKey
      BSTNode problemNode = checkNode(rootNode, null, null, visitedNodes);
      
      // If we found a problem, return that node
      // Otherwise return null to indicate the tree is valid
      return problemNode;
   }
   
   // This helper method checks a single node and its children
   // minKey: if not null, this node's key must be greater than minKey
   // maxKey: if not null, this node's key must be less than maxKey
   // visitedNodes: set of nodes we've seen before
   private static BSTNode checkNode(BSTNode node, Integer minKey, Integer maxKey, Set<BSTNode> visitedNodes) {
      // First, check if we've already visited this node
      // If we have, that means it's a child of multiple parents (a problem!)
      if (visitedNodes.contains(node)) {
         // Return this node as the problematic one
         return node;
      }
      
      // Mark this node as visited
      visitedNodes.add(node);
      
      // Now check if this node's key violates the BST property
      // If minKey is set, this node's key must be greater than minKey
      if (minKey != null) {
         if (node.key <= minKey) {
            // This node's key is too small, return it as the problem
            return node;
         }
      }
      
      // If maxKey is set, this node's key must be less than maxKey
      if (maxKey != null) {
         if (node.key >= maxKey) {
            // This node's key is too large, return it as the problem
            return node;
         }
      }
      
      // Now check the left child if it exists
      if (node.left != null) {
         // For the left child, we need to update the maxKey constraint
         // The left child's key must be less than this node's key
         Integer newMaxKey;
         if (maxKey == null) {
            // There was no previous maxKey, so use this node's key
            newMaxKey = node.key;
         } else {
            // There was a previous maxKey, use whichever is smaller
            if (node.key < maxKey) {
               newMaxKey = node.key;
            } else {
               newMaxKey = maxKey;
            }
         }
         
         // Recursively check the left child
         BSTNode leftResult = checkNode(node.left, minKey, newMaxKey, visitedNodes);
         if (leftResult != null) {
            // Found a problem in the left subtree, return it
            return leftResult;
         }
      }
      
      // Now check the right child if it exists
      if (node.right != null) {
         // For the right child, we need to update the minKey constraint
         // The right child's key must be greater than this node's key
         Integer newMinKey;
         if (minKey == null) {
            // There was no previous minKey, so use this node's key
            newMinKey = node.key;
         } else {
            // There was a previous minKey, use whichever is larger
            if (node.key > minKey) {
               newMinKey = node.key;
            } else {
               newMinKey = minKey;
            }
         }
         
         // Recursively check the right child
         BSTNode rightResult = checkNode(node.right, newMinKey, maxKey, visitedNodes);
         if (rightResult != null) {
            // Found a problem in the right subtree, return it
            return rightResult;
         }
      }
      
      // If we got here, this node and its subtree are valid
      return null;
   }
}