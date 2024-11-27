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
                Node uncle = grandparent.right;

                if (uncle != null && uncle.color == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grandparent.color = RED;
                    node = grandparent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                    }
                    parent.color = BLACK;
                    grandparent.color = RED;
                    rotateRight(grandparent);
                }
            } else {
                Node uncle = grandparent.left;

                if (uncle != null && uncle.color == RED) {
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    grandparent.color = RED;
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                    }
                    parent.color = BLACK;
                    grandparent.color = RED;
                    rotateLeft(grandparent);
                }
            }
        }
        root.color = BLACK;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;

        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;

        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.left) {
            node.parent.left = leftChild;
        } else {
            node.parent.right = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    public void delete(int value) {
        Node node = root;

        while (node != null && node.value != value) {
            if (value < node.value) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (node == null) return;

        Node movedUpNode;
        boolean deletedNodeColor = node.color;

        if (node.left == null) {
            movedUpNode = node.right;
            replaceNode(node, node.right);
        } else if (node.right == null) {
            movedUpNode = node.left;
            replaceNode(node, node.left);
        } else {
            Node successor = findMinimum(node.right);
            deletedNodeColor = successor.color;
            movedUpNode = successor.right;

            if (successor.parent == node) {
                if (movedUpNode != null) movedUpNode.parent = successor;
            } else {
                replaceNode(successor, successor.right);
                successor.right = node.right;
                successor.right.parent = successor;
            }

            replaceNode(node, successor);
            successor.left = node.left;
            successor.left.parent = successor;
            successor.color = node.color;
        }

        if (deletedNodeColor == BLACK) {
            fixDeletion(movedUpNode);
        }
    }

    private void fixDeletion(Node node) {
        while (node != root && (node == null || node.color == BLACK)) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;

                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }

                if ((sibling.left == null || sibling.left.color == BLACK) &&
                    (sibling.right == null || sibling.right.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling.right == null || sibling.right.color == BLACK) {
                        sibling.left.color = BLACK;
                        sibling.color = RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }

                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    if (sibling.right != null) sibling.right.color = BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node sibling = node.parent.left;

                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }

                if ((sibling.right == null || sibling.right.color == BLACK) &&
                    (sibling.left == null || sibling.left.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling.left == null || sibling.left.color == BLACK) {
                        sibling.right.color = BLACK;
                        sibling.color = RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }

                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    if (sibling.left != null) sibling.left.color = BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        if (node != null) node.color = BLACK;
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
