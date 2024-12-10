package org.example.storage.tree.traversal;

import lombok.extern.log4j.Log4j2;
import org.example.storage.node.Node;

@Log4j2
public class PostOrderTraversalStrategy<T extends Comparable<T>> implements TraversalStrategy<T> {
    @Override
    public void traverse(Node<T> node) {
        if (node.left != null) {
            traverse(node.left);
        }
        if (node.right != null) {
            traverse(node.right);
        }
        log.info(String.valueOf(node.value));
    }
}