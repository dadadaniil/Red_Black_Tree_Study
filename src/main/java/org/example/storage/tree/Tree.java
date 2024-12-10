package org.example.storage.tree;

import org.example.storage.node.Node;

import java.util.Optional;

public interface Tree<T extends Comparable<T>> {

    void delete(T value);

    void insert(T value);

    Optional<Node<T>> search(T value);

}
