import java.util.*;

public class BSTNode {   
   public int key;
   public BSTNode left;
   public BSTNode right;

   public BSTNode(int nodeKey, BSTNode leftChild) {
		this(nodeKey, leftChild, null);
	}

   public BSTNode(int nodeKey) {
		this(nodeKey, null, null);
	}

   public BSTNode(int nodeKey, BSTNode leftChild, BSTNode rightChild) {
		key = nodeKey;
		left = leftChild;
		right = rightChild;
	}

   // Counts the number of nodes in this tree
   public int count() {
      int leftCount = 0;
      if (left != null) {
         leftCount = left.count();
      }
      int rightCount = 0;
      if (right != null) {
         rightCount = right.count();
      }
      return 1 + leftCount + rightCount;
   }

   private BSTNode search(int searchKey) {
      BSTNode currentNode = this;
      while (currentNode != null) {
         // Return currentNode if the key matches
         if (currentNode.key == searchKey) {
            return currentNode;
         }
         
         // Branch left or right
         else if (searchKey < currentNode.key) {
            currentNode = currentNode.left;
         }
         else {
            currentNode = currentNode.right;
         }
      }
      
      // Key not found
      return null;
   }

   // Returns true if the tree contains the given key, false otherwise
   public boolean contains(int key) {
      return search(key) != null;
   }

   // Inserts a new key into the subtree rooted at this node, provided the key
   // doesn't already exist.
   boolean insertKey(int key) {
      // Duplicate keys not allowed
      if (contains(key)) {
         return false;
      }
      
      // Allocate the new node
      insertNode(new BSTNode(key));
      return true;
   }

   public void insertAllKeys(ArrayList<Integer> keys) {
      for (int key : keys) {
         insertNode(new BSTNode(key));
      }
   }

   // Inserts the new node into the tree.
   public void insertNode(BSTNode node) {
      BSTNode currentNode = this;
      while (currentNode != null) {
         if (node.key < currentNode.key) {
            if (currentNode.left != null) {
               currentNode = currentNode.left;
            }
            else {
               currentNode.left = node;
               currentNode = null;
            }
         }
         else {
            if (currentNode.right != null) {
               currentNode = currentNode.right;
            }
            else {
               currentNode.right = node;
               currentNode = null;
            }
         }
      }
   }  
}