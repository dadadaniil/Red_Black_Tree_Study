package org.example.storage.tree;

import org.example.storage.node.Node;
import org.example.storage.tree.traversal.TraversalStrategy;

/**
 * I had an idea to also move fixInsertion for delete and insert
 * into a strategy, but decided that it would be madness,
 * which would lead to too much fragmentation of logic across the application.
 */

public abstract class AbstractBalancedTree<T extends Comparable<T>> implements Tree<T> {

    public Node<T> root;

    protected final void fixViolation(Node<T> node) {
        while (node != root && node.parent.color == RedBlackTree.RED) {
            if (node.parent == node.parent.parent.left) {
                handleLeftSubtreeViolation(node);
            } else {
                handleRightSubtreeViolation(node);
            }
        }
        root.color = RedBlackTree.BLACK; // Ensure the root is always black
    }

    protected void updateChildReference(Node<T> node, Node<T> child, boolean isRight) {
        if (isRight) {
            node.right = child;
        } else {
            node.left = child;
        }
        if (child != null) {
            child.parent = node;
        }
    }

    protected void updateParentReference(Node<T> node, Node<T> child) {
        child.parent = node.parent;
        if (node.parent == null) {
            root = child;
        } else if (node == node.parent.left) {
            node.parent.left = child;
        } else {
            node.parent.right = child;
        }
    }

    public void replaceNode(Node<T> oldNode, Node<T> newNode) {
        if (oldNode.parent == null) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }

        if (newNode != null) {
            newNode.parent = oldNode.parent;
        }
    }

    public void traverse(TraversalStrategy<T> strategy) {
        if (root != null) {
            strategy.traverse(root);
        }
    }

    protected abstract void handleLeftSubtreeViolation(Node<T> node);

    protected abstract void handleRightSubtreeViolation(Node<T> node);

}
