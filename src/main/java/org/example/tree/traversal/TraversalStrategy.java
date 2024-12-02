package org.example.tree.traversal;

import org.example.node.Node;

public interface TraversalStrategy<T extends Comparable<T>> {
    void traverse(Node<T> node);
}
