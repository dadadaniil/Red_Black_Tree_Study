package org.example.structures.tree.traversal;

import org.example.structures.node.Node;

public interface TraversalStrategy<T extends Comparable<T>> {
    void traverse(Node<T> node);
}
