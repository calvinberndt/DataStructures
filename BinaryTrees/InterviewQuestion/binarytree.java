//Binary Code
//Check if the binary trees are equal


class Node {
    int data;
    Node left, right;

    Node(int item) {
        data = item;
        left = right = null;
    }
}

class BinaryTree {
    Node root1, root2;

    BinaryTree(Node root1, Node root2) {
        this.root1 = root1;
        this.root2 = root2;
    }

    boolean areEqual() {
        return areEqualHelper(root1, root2);
    }

    private boolean areEqualHelper(Node node1, Node node2) {

        if (node1 == null && node2 == null) {
            return true;
        }
        if(node1.data != node2.data){
            return false;
        }
        if((node1 == null && node2 != null) || (node2 == null && node1 != null)) {
            return false;
        }
    
        return areEqualHelper(node1.left, node2.left) &&
               areEqualHelper(node1.right, node2.right);
    }
    //Count the level of where the binary tree doesn't match:
    int countLevel(Node node1, Node node2, int level) {
        if (node1 == null && node2 == null) {
            return 0;
        }
        if (node1.data != node2.data) {
            return level;
        }
        int leftLevel = countLevel(node1.left, node2.left, level + 1);
        int rightLevel = countLevel(node1.right, node2.right, level + 1);
        return Math.max(leftLevel, rightLevel);
    }
}
class BinaryTreeDemo {
    public static void main(String[] args) {
        //Creating the data structure for node 1 and node 2:
        Node root1 = new Node(1);
        root1.left = new Node(2);
        root1.right = new Node(3);
        //add a layer of depth to test the non-equal case:
        root1.left.left = new Node(4);
        root1.right.right = new Node(5);
        Node root2 = new Node(1);
        root2.left = new Node(2);
        root2.right = new Node(3);
        BinaryTree tree = new BinaryTree(root1, root2);
        System.out.println(tree.areEqual());
        //Using recursion on the BinaryTrees to check if they are equal or not
    }
}
