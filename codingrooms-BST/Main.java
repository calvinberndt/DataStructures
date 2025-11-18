import java.util.*;

public class Main {
   public static void main(String[] args) {
      // Tree 1 - invalid
      // Tree 1 is the left tree in the "Examples of key-related problems" figure
      System.out.print("Tree 1: ");
      BSTNode tree1Node50 = new BSTNode(
         50, 
         new BSTNode(20), 
         new BSTNode(60)
      );
      BSTNode root1 = new BSTNode(
         40, 
         tree1Node50, 
         new BSTNode(
            80, 
            new BSTNode(70), 
            new BSTNode(90)
         )
      );
      testTree(root1, tree1Node50);

      // Tree 2 - invalid
      // Right tree in the "Examples of key-related problems" figure
      System.out.print("Tree 2: ");
      BSTNode tree2Node66 = new BSTNode(66);
      BSTNode root2 = new BSTNode(
         77,
         new BSTNode(
            44,
            new BSTNode(33),
            new BSTNode(55)
         ),
         new BSTNode(
            88,
            tree2Node66,
            new BSTNode(99)
         )
      );
      testTree(root2, tree2Node66);

      // Tree 3 - valid
      // Randomly generated tree
      System.out.print("Tree 3: ");
      testTree(makeRandomTree(), null);
   
      // Tree 4 - invalid
      // Left tree in the "Examples of child-related problems" figure
      System.out.print("Tree 4: ");
      BSTNode tree4Node75 = new BSTNode(75, null, new BSTNode(88));
      BSTNode root4 = new BSTNode(
         50,
         new BSTNode(
            25,
            null,
            new BSTNode(37, null, tree4Node75)
         ),
         tree4Node75
      );
      testTree(root4, tree4Node75);

      // Tree 5 - invalid
      // Right tree in the "Examples of child-related problems" figure
      System.out.print("Tree 5: ");
      BSTNode tree5Node55 = new BSTNode(55);
      BSTNode root5 = new BSTNode(
         44,
         new BSTNode(22, new BSTNode(11), new BSTNode(33)),
         tree5Node55
      );
      tree5Node55.right = root5;
      testTree(root5, root5);
   
      // Tree 6 - invalid
      // Tree 6's node 13 is the left child of both node 14 and node 86
      System.out.print("Tree 6: ");
      BSTNode tree6Node13 = new BSTNode(
         13,
         new BSTNode(
            12,
            new BSTNode(8, new BSTNode(6), new BSTNode(10)),
            null
         ),
         null
      );
      BSTNode tree6Node86 = new BSTNode(86, tree6Node13, new BSTNode(87));
      BSTNode root6 = new BSTNode(
         38,
         new BSTNode(14, tree6Node13, null),
         new BSTNode(
            66,
            null,
            new BSTNode(
               81,
               new BSTNode(73),
               new BSTNode(88, tree6Node86, new BSTNode(91))
            )
         )
      );
      testTree(root6, tree6Node13);

      // Trees 7 and 8 - valid
      // Randomly generated trees
      System.out.print("Tree 7: ");
      testTree(makeRandomTree(), null);
      System.out.print("Tree 8: ");
      testTree(makeRandomTree(), null);

      // Tree 9 - invalid
      // Duplicate key 63
      System.out.print("Tree 9: ");
      BSTNode tree9Node63Lower = new BSTNode(63);
      BSTNode root9 = new BSTNode(
         63,
         new BSTNode(
            47,
            null,
            new BSTNode(57, new BSTNode(50), null)
         ),
         new BSTNode(
            77,
            tree9Node63Lower,
            new BSTNode(88, new BSTNode(71), new BSTNode(89))
         )
      );
      testTree(root9, tree9Node63Lower);

      // Tree 10 - invalid
      // Several duplicate keys. Leftmost leaf with 25 is the first problematic
      // node.
      System.out.print("Tree 10: ");
      BSTNode tree10Node25Leftmost = new BSTNode(25);
      BSTNode root10 = new BSTNode(
         50,
         new BSTNode(
            25,
            new BSTNode(12, new BSTNode(10), tree10Node25Leftmost),
            new BSTNode(37, new BSTNode(25), new BSTNode(50))
         ),
         new BSTNode(
            75,
            new BSTNode(62, new BSTNode(55), new BSTNode(68)),
            new BSTNode(88, new BSTNode(75), new BSTNode(89))
         )
      );
      testTree(root10, tree10Node25Leftmost);
   }

   // Makes and returns a randomly generated, valid BST with at least 10,000 nodes
   static BSTNode makeRandomTree() {
      Random rand = new Random();

      // Determine a sufficiently large tree size
      int treeSize = 10000 + rand.nextInt(10000);

      // Make the tree's root
      BSTNode root = new BSTNode(rand.nextInt(100000));

      // Add random keys until the desired size is reached
      int keyCount = 1;
      while (keyCount < treeSize) {
         if (root.insertKey(rand.nextInt(100000))) {
            keyCount++;
         }
      }

      return root;
   }

   // Performs a test of BSTChecker::CheckBSTValidity() given the tree's root and
   // and expected return value
   static boolean testTree(BSTNode rootNode, BSTNode expected) {
      BSTNode badNode = BSTChecker.checkBSTValidity(rootNode);

      boolean pass = false;
      if (badNode == expected) {
         System.out.print("PASS: checkBSTValidity() returned ");
         if (expected != null) {
            System.out.println("the node with key " + expected.key);
         }
         else {
            System.out.println("null for a valid tree");
         }
         pass = true;
      }
      else {
         System.out.print("FAIL: checkBSTValidity() returned ");

         // Special case message if badNode and expected are non-null with equal
         // keys
         if (badNode != null && expected != null && badNode.key == expected.key) {
            System.out.println("a node with key " + badNode.key + ", but not the "
                  + "correct node. During a preorder traversal, the node with the "
                  + "second occurrence of a duplicate key must be returned.");
         }
         else if (badNode != null) {
            System.out.print("the node with key " + badNode.key + ", but should " + "have returned ");
            if (expected != null) {
               System.out.println("the node with key " + expected.key);
            }
            else {
               System.out.println("null since the tree is valid");
            }
         }
         else {
            System.out.println("null for an invalid tree");
         }
      }

      return pass;
   }
}
