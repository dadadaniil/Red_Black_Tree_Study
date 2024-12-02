import org.example.tree.RedBlackTree;
import org.example.node.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {

    @Test
    void testRotateLeft() {
        RedBlackTree tree = new RedBlackTree();
        Node root = new Node(10);
        Node rightChild = new Node(20);
        Node rightLeftChild = new Node(15);

        root.right = rightChild;
        rightChild.parent = root;
        rightChild.left = rightLeftChild;
        rightLeftChild.parent = rightChild;

        tree.rotateLeft(root);

        assertEquals(rightChild, tree.root);
        assertEquals(root, rightChild.left);
        assertEquals(rightLeftChild, root.right);
        assertEquals(rightChild, root.parent);
        assertEquals(root, rightLeftChild.parent);
    }

    @Test
    void testRotateRight() {
        RedBlackTree tree = new RedBlackTree();
        Node root = new Node(20);
        Node leftChild = new Node(10);
        Node leftRightChild = new Node(15);

        root.left = leftChild;
        leftChild.parent = root;
        leftChild.right = leftRightChild;
        leftRightChild.parent = leftChild;

        tree.rotateRight(root);

        assertEquals(leftChild, tree.root);
        assertEquals(root, leftChild.right);
        assertEquals(leftRightChild, root.left);
        assertEquals(leftChild, root.parent);
        assertEquals(root, leftRightChild.parent);
    }

    @Test
    void testRecolorParentAndSibling() {
        RedBlackTree tree = new RedBlackTree();
        Node parent = new Node(10);
        Node sibling = new Node(20);
        Node siblingRight = new Node(30);

        parent.color = RedBlackTree.BLACK;
        sibling.color = RedBlackTree.RED;
        sibling.right = siblingRight;

        tree.recolorParentAndSibling(parent, sibling);

        assertEquals(RedBlackTree.BLACK, parent.color);
        assertEquals(RedBlackTree.BLACK, sibling.color);
        assertEquals(RedBlackTree.BLACK, siblingRight.color);
    }

    @Test
    void testIsBlack() {
        RedBlackTree tree = new RedBlackTree();
        Node blackNode = new Node(10);
        blackNode.color = RedBlackTree.BLACK;
        Node redNode = new Node(20);
        redNode.color = RedBlackTree.RED;

        assertTrue(tree.isBlack(blackNode));
        assertFalse(tree.isBlack(redNode));
        assertTrue(tree.isBlack(null));
    }

    @Test
    void testIsLeftChild() {
        RedBlackTree tree = new RedBlackTree();
        Node parent = new Node(10);
        Node leftChild = new Node(5);
        Node rightChild = new Node(15);

        parent.left = leftChild;
        parent.right = rightChild;
        leftChild.parent = parent;
        rightChild.parent = parent;

        assertTrue(tree.isLeftChild(leftChild));
        assertFalse(tree.isLeftChild(rightChild));
    }

    @Test
    void testReplaceNode() {
        RedBlackTree tree = new RedBlackTree();
        Node oldNode = new Node(10);
        Node newNode = new Node(20);
        Node parent = new Node(30);

        oldNode.parent = parent;
        parent.left = oldNode;

        tree.replaceNode(oldNode, newNode);

        assertEquals(newNode, parent.left);
        assertEquals(parent, newNode.parent);
    }

    @Test
    void testFindMinimum() {
        RedBlackTree tree = new RedBlackTree();
        Node root = new Node(10);
        Node left = new Node(5);
        Node right = new Node(15);
        Node leftLeft = new Node(2);

        root.left = left;
        root.right = right;
        left.left = leftLeft;

        assertEquals(leftLeft, tree.findMinimum(root));
        assertEquals(leftLeft, tree.findMinimum(left));
    }
}