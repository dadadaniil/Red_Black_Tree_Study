package org.example;

public class RedBlackTree {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private Node root;

    private static class Node {
        int value;
        boolean color;
        Node left, right, parent;

        Node(int value) {
            this.value = value;
            this.color = RED; // New nodes are always red
        }
    }

    public void insert(int value) {
        Node newNode = new Node(value);
        if (root == null) {
            root = newNode;
            root.color = BLACK;
            return;
        }

        Node current = root, parent = null;

        while (current != null) {
            parent = current;
            if (value < current.value) {
                current = current.left;
            } else if (value > current.value) {
                current = current.right;
            } else {
                return; // Ignore duplicates
            }
        }

        newNode.parent = parent;
        if (value < parent.value) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixInsertion(newNode);
    }

    private void fixInsertion(Node node) {
        while (node != root && node.parent.color == RED) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (parent == grandparent.left) {
                handleLeftSubtreeInsertion(node, parent, grandparent);
            } else {
                handleRightSubtreeInsertion(node, parent, grandparent);
            }
        }
        root.color = BLACK;
    }

    private void handleLeftSubtreeInsertion(Node node, Node parent, Node grandparent) {
        Node uncle = grandparent.right;

        // Case 1: Uncle is red (Recoloring)
        if (uncle != null && uncle.color == RED) {
            recolorAfterInsertion(parent, uncle, grandparent);
            return;
        }

        // Case 2a: Node is the parent's right child (Left Rotation)
        if (node == parent.right) {
            node = parent;
            rotateLeft(node);
        }

        // Case 2b: Node is the parent's left child (Right Rotation)
        parent.color = BLACK;
        grandparent.color = RED;
        rotateRight(grandparent);
    }

    private void handleRightSubtreeInsertion(Node node, Node parent, Node grandparent) {
        Node uncle = grandparent.left;

        // Case 1: Uncle is red (Recoloring)
        if (uncle != null && uncle.color == RED) {
            recolorAfterInsertion(parent, uncle, grandparent);
            return;
        }

        // Case 2a: Node is the parent's left child (Right Rotation)
        if (node == parent.left) {
            node = parent;
            rotateRight(node);
        }

        // Case 2b: Node is the parent's right child (Left Rotation)
        parent.color = BLACK;
        grandparent.color = RED;
        rotateLeft(grandparent);
    }

    private void recolorAfterInsertion(Node parent, Node uncle, Node grandparent) {
        parent.color = BLACK;
        uncle.color = BLACK;
        grandparent.color = RED;
    }


    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        if (rightChild == null) return; // No rotation possible

        // Step 1: Update node's right child to rightChild's left subtree
        updateChildReference(node, rightChild.left, true);

        // Step 2: Update parent of rightChild
        updateParentReference(node, rightChild);

        // Step 3: Reassign node as left child of rightChild
        rightChild.left = node;
        node.parent = rightChild;

        // Step 4: Update root if needed
        if (node == root) {
            root = rightChild;
        }
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        if (leftChild == null) return; // No rotation possible

        // Step 1: Update node's left child to leftChild's right subtree
        updateChildReference(node, leftChild.right, false);

        // Step 2: Update parent of leftChild
        updateParentReference(node, leftChild);

        // Step 3: Reassign node as right child of leftChild
        leftChild.right = node;
        node.parent = leftChild;

        // Step 4: Update root if needed
        if (node == root) {
            root = leftChild;
        }
    }

    private void updateChildReference(Node node, Node child, boolean isRight) {
        if (isRight) {
            node.right = child;
        } else {
            node.left = child;
        }
        if (child != null) {
            child.parent = node;
        }
    }

    private void updateParentReference(Node node, Node child) {
        child.parent = node.parent;
        if (node.parent == null) {
            root = child;
        } else if (node == node.parent.left) {
            node.parent.left = child;
        } else {
            node.parent.right = child;
        }
    }


    public void delete(int value) {
        Node nodeToDelete = findNode(value);
        if (nodeToDelete == null) return;

        Node movedUpNode;
        boolean deletedNodeColor = nodeToDelete.color;

        if (nodeToDelete.left == null) {
            movedUpNode = handleSingleChildOrLeafNodeDeletion(nodeToDelete, nodeToDelete.right);
        } else if (nodeToDelete.right == null) {
            movedUpNode = handleSingleChildOrLeafNodeDeletion(nodeToDelete, nodeToDelete.left);
        } else {
            movedUpNode = handleTwoChildNodeDeletion(nodeToDelete);
            deletedNodeColor = movedUpNode.color;
        }

        if (deletedNodeColor == BLACK) {
            fixDeletion(movedUpNode);
        }
    }

    private Node findNode(int value) {
        Node current = root;
        while (current != null && current.value != value) {
            if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return current;
    }

    private Node handleSingleChildOrLeafNodeDeletion(Node nodeToDelete, Node child) {
        replaceNode(nodeToDelete, child);
        return child;
    }

    private Node handleTwoChildNodeDeletion(Node nodeToDelete) {
        Node successor = findMinimum(nodeToDelete.right);
        Node movedUpNode = successor.right;

        if (successor.parent != nodeToDelete) {
            replaceNode(successor, successor.right);
            successor.right = nodeToDelete.right;
            if (successor.right != null) {
                successor.right.parent = successor;
            }
        }

        replaceNode(nodeToDelete, successor);
        successor.left = nodeToDelete.left;
        if (successor.left != null) {
            successor.left.parent = successor;
        }
        successor.color = nodeToDelete.color;

        return movedUpNode;
    }


    private void fixDeletion(Node node) {
        while (node != root && isBlack(node)) {
            if (isLeftChild(node)) {
                handleLeftSiblingCase(node);
            } else {
                handleRightSiblingCase(node);
            }
        }

        if (node != null) {
            node.color = BLACK;
        }
    }

    private void handleLeftSiblingCase(Node node) {
        Node sibling = node.parent.right;

        if (sibling.color == RED) {
            recolorAndRotateLeft(node.parent, sibling);
            sibling = node.parent.right;
        }

        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            recolorSiblingRed(sibling);
            node = node.parent;
        } else {
            if (isBlack(sibling.right)) {
                recolorAndRotateRight(sibling, sibling.left);
                sibling = node.parent.right;
            }
            recolorParentAndSibling(node.parent, sibling);
            rotateLeft(node.parent);
            node = root;
        }
    }

    private void handleRightSiblingCase(Node node) {
        Node sibling = node.parent.left;

        if (sibling.color == RED) {
            recolorAndRotateRight(node.parent, sibling);
            sibling = node.parent.left;
        }

        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            recolorSiblingRed(sibling);
            node = node.parent;
        } else {
            if (isBlack(sibling.left)) {
                recolorAndRotateLeft(sibling, sibling.right);
                sibling = node.parent.left;
            }
            recolorParentAndSibling(node.parent, sibling);
            rotateRight(node.parent);
            node = root;
        }
    }

    private void recolorAndRotateLeft(Node parent, Node sibling) {
        sibling.color = BLACK;
        parent.color = RED;
        rotateLeft(parent);
    }

    private void recolorAndRotateRight(Node parent, Node sibling) {
        sibling.color = BLACK;
        parent.color = RED;
        rotateRight(parent);
    }

    private void recolorSiblingRed(Node sibling) {
        sibling.color = RED;
    }

    private void recolorParentAndSibling(Node parent, Node sibling) {
        sibling.color = parent.color;
        parent.color = BLACK;
        if (sibling.right != null) {
            sibling.right.color = BLACK;
        }
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == BLACK;
    }

    private boolean isLeftChild(Node node) {
        return node == node.parent.left;
    }


    private void replaceNode(Node oldNode, Node newNode) {
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

    private Node findMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public String visualize() {
        StringBuilder sb = new StringBuilder();
        visualizeHelper(root, sb, "", true);
        return sb.toString();
    }

    private void visualizeHelper(Node node, StringBuilder sb, String prefix, boolean isTail) {
        if (node == null) return;

        if (node.right != null) {
            visualizeHelper(node.right, sb, prefix + (isTail ? "│   " : "    "), false);
        }

        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.value).append(node.color == RED ? " (R)" : " (B)").append("\n");

        if (node.left != null) {
            visualizeHelper(node.left, sb, prefix + (isTail ? "    " : "│   "), true);
        }
    }

}
