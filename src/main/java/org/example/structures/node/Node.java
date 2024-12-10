package org.example.structures.node;

import org.example.structures.tree.RedBlackTree;

public class Node<T> {
    public T value;
    public boolean color;
    public Node<T> left;
    public Node<T> right;
    public Node<T> parent;

    public Node(T value) {
        this.value = value;
        this.color = RedBlackTree.RED; // New nodes are always red
    }
}
