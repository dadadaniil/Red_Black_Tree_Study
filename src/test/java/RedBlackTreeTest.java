import static org.junit.jupiter.api.Assertions.*;

import org.example.RedBlackTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedBlackTreeTest {

    private RedBlackTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new RedBlackTree<>();
    }

    @Test
    void testLeftRotation_simpleCase() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(20);
        RedBlackTree<Integer> rightLeftChild = new RedBlackTree<>(15);

        root.right = rightChild;
        rightChild.parent = root;
        rightChild.left = rightLeftChild;
        rightLeftChild.parent = rightChild;

        tree.leftRotation(root);

        assertEquals(20, root.parent.value);
        assertEquals(10, root.parent.left.value);
        assertEquals(15, root.parent.left.right.value);
    }

    @Test
    void testLeftRotation_withLeftChild() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(5);

        root.right = rightChild;
        root.left = leftChild;
        rightChild.parent = root;
        leftChild.parent = root;

        tree.leftRotation(root);

        assertEquals(20, root.parent.value);
        assertEquals(10, root.parent.left.value);
        assertEquals(5, root.parent.left.left.value);
    }

    @Test
    void testLeftRotation_withRightChild() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(20);
        RedBlackTree<Integer> rightRightChild = new RedBlackTree<>(25);

        root.right = rightChild;
        rightChild.parent = root;
        rightChild.right = rightRightChild;
        rightRightChild.parent = rightChild;

        tree.leftRotation(root);

        assertEquals(20, root.parent.value);
        assertEquals(10, root.parent.left.value);
        assertEquals(25, root.parent.right.value);
    }

    @Test
    void testLeftRotation_withBothChildren() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(5);
        RedBlackTree<Integer> rightLeftChild = new RedBlackTree<>(15);

        root.right = rightChild;
        root.left = leftChild;
        rightChild.parent = root;
        leftChild.parent = root;
        rightChild.left = rightLeftChild;
        rightLeftChild.parent = rightChild;

        tree.leftRotation(root);

        assertEquals(20, root.parent.value);
        assertEquals(10, root.parent.left.value);
        assertEquals(5, root.parent.left.left.value);
        assertEquals(15, root.parent.left.right.value);
    }

    @Test
    void testRightRotation_simpleCase() {
        RedBlackTree<Integer> root = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(10);
        RedBlackTree<Integer> leftRightChild = new RedBlackTree<>(15);

        root.left = leftChild;
        leftChild.parent = root;
        leftChild.right = leftRightChild;
        leftRightChild.parent = leftChild;

        tree.rightRotation(root);

        assertEquals(10, root.parent.value);
        assertEquals(20, root.parent.right.value);
        assertEquals(15, root.parent.right.left.value);
    }

    @Test
    void testRightRotation_withRightChild() {
        RedBlackTree<Integer> root = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(10);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(25);

        root.left = leftChild;
        root.right = rightChild;
        leftChild.parent = root;
        rightChild.parent = root;

        tree.rightRotation(root);

        assertEquals(10, root.parent.value);
        assertEquals(20, root.parent.right.value);
        assertEquals(25, root.parent.right.right.value);
    }

    @Test
    void testRightRotation_withLeftChild() {
        RedBlackTree<Integer> root = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(10);
        RedBlackTree<Integer> leftLeftChild = new RedBlackTree<>(5);

        root.left = leftChild;
        leftChild.parent = root;
        leftChild.left = leftLeftChild;
        leftLeftChild.parent = leftChild;

        tree.rightRotation(root);

        assertEquals(10, root.parent.value);
        assertEquals(20, root.parent.right.value);
        assertEquals(5, root.parent.left.value);
    }

    @Test
    void testRightRotation_withBothChildren() {
        RedBlackTree<Integer> root = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(10);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(25);
        RedBlackTree<Integer> leftRightChild = new RedBlackTree<>(15);

        root.left = leftChild;
        root.right = rightChild;
        leftChild.parent = root;
        rightChild.parent = root;
        leftChild.right = leftRightChild;
        leftRightChild.parent = leftChild;

        tree.rightRotation(root);

        assertEquals(10, root.parent.value);
        assertEquals(20, root.parent.right.value);
        assertEquals(25, root.parent.right.right.value);
        assertEquals(15, root.parent.right.left.value);
    }

    @Test
    void testRightRotation_RootNode() {
        RedBlackTree<Integer> root = new RedBlackTree<>(20);
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(10);
        RedBlackTree<Integer> leftRightChild = new RedBlackTree<>(15);

        root.left = leftChild;
        leftChild.parent = root;
        leftChild.right = leftRightChild;
        leftRightChild.parent = leftChild;

        tree.rightRotation(root);

        assertEquals(10, root.parent.value);
        assertEquals(20, root.parent.right.value);
        assertEquals(15, root.parent.right.left.value);
        assertNull(root.parent.parent);
    }

    @Test
    void testRedViolationWithParentRed() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        RedBlackTree<Integer> parent = new RedBlackTree<>(10);
        RedBlackTree<Integer> node = new RedBlackTree<>(15);

        parent.colour = true;
        node.colour = true;
        node.parent = parent;

        assertFalse(tree.redViolationsCheck(node));
    }

    @Test
    void testRedViolationWithoutParentRed() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        RedBlackTree<Integer> parent = new RedBlackTree<>(10);
        RedBlackTree<Integer> node = new RedBlackTree<>(15);

        parent.colour = false;
        node.colour = true;
        node.parent = parent;

        assertTrue(tree.redViolationsCheck(node));
    }

    @Test
    void testRedViolationWithNullParent() {
        RedBlackTree<Integer> initialTree = new RedBlackTree<>();
        RedBlackTree<Integer> node = new RedBlackTree<>(10);

        node.colour = true;

        assertTrue(initialTree.redViolationsCheck(node));
    }

    @Test
    void testBlackViolationCheck_ValidBlackHeight() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        root.colour = false;
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(5, root, null, null, false);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(15, root, null, null, false);
        root.left = leftChild;
        root.right = rightChild;

        assertTrue(tree.blackViolationCheck(root));
    }

    @Test
    void testBlackViolationCheck_InvalidBlackHeight() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        root.colour = false;
        RedBlackTree<Integer> leftChild = new RedBlackTree<>(5, root, null, null, false);
        RedBlackTree<Integer> rightChild = new RedBlackTree<>(15, root, null, null, true);
        root.left = leftChild;
        root.right = rightChild;
        rightChild.left = new RedBlackTree<>(12, rightChild, null, null, false);

        assertFalse(tree.blackViolationCheck(root));
    }

    @Test
    void testBlackViolationCheck_NullNode() {
        assertTrue(tree.blackViolationCheck(null));
    }

    @Test
    void testBlackViolationCheck_SingleBlackNode() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        root.colour = false;

        assertTrue(tree.blackViolationCheck(root));
    }

    @Test
    void testBlackViolationCheck_SingleRedNode() {
        RedBlackTree<Integer> root = new RedBlackTree<>(10);
        root.colour = true;

        assertTrue(tree.blackViolationCheck(root));
    }

}
