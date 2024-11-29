package org.example.tree;

import org.example.node.Node;

public interface Tree<T extends Comparable<T>> {

    void delete(T value);

    void insert(T value);

    Node<T> search(T value);

}
