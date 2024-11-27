import org.example.RedBlackTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {

    @Test
    void testRotateLeft() {
        RedBlackTree tree = new RedBlackTree();
        RedBlackTree.Node root = new RedBlackTree.Node(10);
        RedBlackTree.Node rightChild = new RedBlackTree.Node(20);
        RedBlackTree.Node rightLeftChild = new RedBlackTree.Node(15);

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
        RedBlackTree.Node root = new RedBlackTree.Node(20);
        RedBlackTree.Node leftChild = new RedBlackTree.Node(10);
        RedBlackTree.Node leftRightChild = new RedBlackTree.Node(15);

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
        RedBlackTree.Node parent = new RedBlackTree.Node(10);
        RedBlackTree.Node sibling = new RedBlackTree.Node(20);
        RedBlackTree.Node siblingRight = new RedBlackTree.Node(30);

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
        RedBlackTree.Node blackNode = new RedBlackTree.Node(10);
        blackNode.color = RedBlackTree.BLACK;
        RedBlackTree.Node redNode = new RedBlackTree.Node(20);
        redNode.color = RedBlackTree.RED;

        assertTrue(tree.isBlack(blackNode));
        assertFalse(tree.isBlack(redNode));
        assertTrue(tree.isBlack(null));
    }

    @Test
    void testIsLeftChild() {
        RedBlackTree tree = new RedBlackTree();
        RedBlackTree.Node parent = new RedBlackTree.Node(10);
        RedBlackTree.Node leftChild = new RedBlackTree.Node(5);
        RedBlackTree.Node rightChild = new RedBlackTree.Node(15);

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
        RedBlackTree.Node oldNode = new RedBlackTree.Node(10);
        RedBlackTree.Node newNode = new RedBlackTree.Node(20);
        RedBlackTree.Node parent = new RedBlackTree.Node(30);

        oldNode.parent = parent;
        parent.left = oldNode;

        tree.replaceNode(oldNode, newNode);

        assertEquals(newNode, parent.left);
        assertEquals(parent, newNode.parent);
    }

    @Test
    void testFindMinimum() {
        RedBlackTree tree = new RedBlackTree();
        RedBlackTree.Node root = new RedBlackTree.Node(10);
        RedBlackTree.Node left = new RedBlackTree.Node(5);
        RedBlackTree.Node right = new RedBlackTree.Node(15);
        RedBlackTree.Node leftLeft = new RedBlackTree.Node(2);

        root.left = left;
        root.right = right;
        left.left = leftLeft;

        assertEquals(leftLeft, tree.findMinimum(root));
        assertEquals(leftLeft, tree.findMinimum(left));
    }
}