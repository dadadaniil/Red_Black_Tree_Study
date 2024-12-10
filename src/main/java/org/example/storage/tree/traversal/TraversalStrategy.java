package org.example.storage.tree.traversal;

import org.example.storage.node.Node;

public interface TraversalStrategy<T extends Comparable<T>> {
    void traverse(Node<T> node);
}
