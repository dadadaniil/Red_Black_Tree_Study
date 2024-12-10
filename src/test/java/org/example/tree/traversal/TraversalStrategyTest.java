package org.example.tree.traversal;

import org.example.storage.node.Node;
import org.example.storage.tree.traversal.InOrderTraversalStrategy;
import org.example.storage.tree.traversal.PostOrderTraversalStrategy;
import org.example.storage.tree.traversal.PreOrderTraversalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TraversalStrategyTest {

    private Node<Integer> root;

    @BeforeEach
    void setUp() {
        root = new Node<>(20);
        root.left = new Node<>(10);
        root.right = new Node<>(30);
        root.left.left = new Node<>(5);
        root.left.right = new Node<>(15);
        root.right.left = new Node<>(25);
        root.right.right = new Node<>(35);
    }

    @Test
    void testInOrderTraversal() {
        InOrderTraversalStrategy<Integer> inOrderTraversal = new InOrderTraversalStrategy<>();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        inOrderTraversal.traverse(root);

        System.setOut(originalOut);
        String expectedOutput = "5\n10\n15\n20\n25\n30\n35\n";
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void testPreOrderTraversal() {
        PreOrderTraversalStrategy<Integer> preOrderTraversal = new PreOrderTraversalStrategy<>();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        preOrderTraversal.traverse(root);

        System.setOut(originalOut);
        String expectedOutput = "20\n10\n5\n15\n30\n25\n35\n";
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void testPostOrderTraversal() {
        PostOrderTraversalStrategy<Integer> postOrderTraversal = new PostOrderTraversalStrategy<>();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        postOrderTraversal.traverse(root);

        System.setOut(originalOut);
        String expectedOutput = "5\n15\n10\n25\n35\n30\n20\n";
        assertEquals(expectedOutput, outContent.toString().trim());
    }
}