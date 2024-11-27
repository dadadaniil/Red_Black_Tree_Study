package org.example;

public class Node<T extends Comparable<T>> {
    T value;
    Node<T> parent;
    Node<T> left;
    Node<T> right;
    boolean colour; // true-red, false-black

    public void insert(T key) {
        Node<T> node = new Node<>(key);
        if (this.value == null) {
            this.value = key;
            this.colour = false;
            return;
        }

        Node<T> current = this;
        Node<T> parentNode = null;

        while (current != null) {
            parentNode = current;
            if (key.compareTo(current.value) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        node.parent = parentNode;

        if (key.compareTo(parentNode.value) < 0) {
            parentNode.left = node;
        } else {
            parentNode.right = node;
        }

        fixInsertionViolation(node);
    }

    public void delete(T key) {
        Node<T> node = findNode(key);
        if (node == null) return;

        Node<T> y = node;
        Node<T> x;
        boolean yOriginalColour = y.colour;

        if (node.left == null) {
            x = node.right;
            transplant(node, node.right);
        } else if (node.right == null) {
            x = node.left;
            transplant(node, node.left);
        } else {
            y = minimum(node.right);
            yOriginalColour = y.colour;
            x = y.right;
            if (y.parent == node) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.colour = node.colour;
        }

        if (!yOriginalColour) {
            fixDeletionViolation(x);
        }
    }

    private Node<T> findNode(T key) {
        Node<T> current = this;
        while (current != null && !key.equals(current.value)) {
            if (key.compareTo(current.value) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return current;
    }

    private void transplant(Node<T> u, Node<T> v) {
        if (u.parent == null) {
            this.value = (v != null) ? v.value : null;
            this.left = (v != null) ? v.left : null;
            this.right = (v != null) ? v.right : null;
            this.colour = v != null && v.colour;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private Node<T> minimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    public void leftRotation(Node node) {
        Node y = node.right;

        node.right = y.left;
        if (y.left != null) {
            y.left.parent = node;
        }

        y.parent = node.parent;
        if (node.parent != null) {
            if (node == node.parent.left) {
                node.parent.left = y;
            } else {
                node.parent.right = y;
            }
        }
        y.left = node;
        node.parent = y;
    }

    public void rightRotation(Node node) {
        Node y = node.left;
        node.left = y.right;

        if (y.right != null) {
            y.right.parent = node;
        }
        y.parent = node.parent;
        if (node.parent != null) {
            if (node == node.parent.right) {
                node.parent.right = y;
            } else {
                node.parent.left = y;
            }
        }
        y.right = node;
        node.parent = y;
    }

    public boolean isHealthy(Node node) {
        return (redViolationsCheck(node) &&
            blackViolationCheck(node) &&
            blackRootCheck(node));
    }

    private boolean redViolationsCheck(Node node) {
        if (!node.colour) {
            return true;
        }
        return node.parent == null || !node.parent.colour;
    }


    private boolean blackViolationCheck(Node node) {
        if (node == null) return true;
        return calculateBlackHeight(node) != -1;
    }

    private int calculateBlackHeight(Node node) {
        if (node == null) {
            return 1;
        }
        int leftHeight = calculateBlackHeight(node.left);
        int rightHeight = calculateBlackHeight(node.right);

        if (rightHeight == -1 ||
            (leftHeight != rightHeight)) {
            return -1;
        }
        return leftHeight + (node.colour ? 0 : 1);
    }

    private boolean blackRootCheck(Node node) {
        while (node.parent != null) {
            node = node.parent;
        }
        return !node.colour;
    }

    private void fixDeletionViolation(Node node) {
        while (node.parent != null && !node.colour) {
            Node parentNode = node.parent;
            Node sibling = (node == parentNode.left) ? parentNode.right : parentNode.left;

            if (sibling.colour) {
                handleRedSibling(node, parentNode, sibling);
                sibling = (node == parentNode.left) ? parentNode.right : parentNode.left;
            }

            if (isBlackWithBlackChildren(sibling)) {
                handleBlackSiblingWithBlackChildren(node, sibling);
                node = parentNode;
            } else {
                handleBlackSiblingWithRedChild(node, parentNode, sibling);
                break;
            }
        }
        node.colour = false;
    }

    private void handleRedSibling(Node node, Node parent, Node sibling) {
        sibling.colour = false;
        parent.colour = true;
        if (node == parent.left) {
            leftRotation(parent);
        } else {
            rightRotation(parent);
        }
    }

    private void handleBlackSiblingWithBlackChildren(Node node, Node sibling) {
        sibling.colour = true;
    }

    private void handleBlackSiblingWithRedChild(Node node, Node parent, Node sibling) {
        if (node == parent.left && (sibling.right == null || !sibling.right.colour)) {
            sibling.left.colour = false;
            sibling.colour = true;
            rightRotation(sibling);
            sibling = parent.right;
        } else if (node == parent.right && (sibling.left == null || !sibling.left.colour)) {
            sibling.right.colour = false;
            sibling.colour = true;
            leftRotation(sibling);
            sibling = parent.left;
        }

        sibling.colour = parent.colour;
        parent.colour = false;
        if (node == parent.left) {
            sibling.right.colour = false;
            leftRotation(parent);
        } else {
            sibling.left.colour = false;
            rightRotation(parent);
        }
    }

    private boolean isBlackWithBlackChildren(Node sibling) {
        return sibling != null &&
            !sibling.colour &&
            (sibling.left == null || !sibling.left.colour) &&
            (sibling.right == null || !sibling.right.colour);
    }


    private void fixInsertionViolation(Node node) {
        if (redViolationsCheck(node)) {
            return;
        }

        Node parentNode = node.parent;
        Node grandparentNode = parentNode.parent;
        Node uncleNode = (grandparentNode.left == parentNode) ? grandparentNode.right : grandparentNode.left;

        if (uncleNode != null && uncleNode.colour) {
            handleRedUncleCase(parentNode, grandparentNode, uncleNode);
        } else if (isZigZag(node, parentNode, grandparentNode)) {
            handleZigZagCase(node, parentNode, grandparentNode);
        } else {
            handleZigZigCase(node, parentNode, grandparentNode);
        }
    }

    private void handleRedUncleCase(Node parent, Node grandparent, Node uncle) {
        parent.colour = false;
        uncle.colour = false;
        grandparent.colour = true;
        fixInsertionViolation(grandparent);
    }

    private void handleZigZagCase(Node node, Node parent, Node grandparent) {
        if (node == parent.right) {
            leftRotation(parent);
            node = node.left;
        } else {
            rightRotation(parent);
            node = node.right;
        }
        handleZigZigCase(node, parent, grandparent);
    }


    private void handleZigZigCase(Node node, Node parent, Node grandparent) {
        parent.colour = false;
        grandparent.colour = true;
        if (parent == grandparent.left) {
            rightRotation(grandparent);
        } else {
            leftRotation(grandparent);
        }
    }

    private boolean isZigZag(Node node, Node parent, Node grandparent) {
        return (parent == grandparent.left && node == parent.right) ||
            (parent == grandparent.right && node == parent.left);
    }

    public String visualize() {
        StringBuilder sb = new StringBuilder();
        visualizeHelper(this, "", true, sb);
        return sb.toString();
    }

    private void visualizeHelper(Node<T> node, String prefix, boolean isTail, StringBuilder sb) {
        if (node.right != null) {
            visualizeHelper(node.right, prefix + (isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.value).append(node.colour ? " (R)" : " (B)").append("\n");
        if (node.left != null) {
            visualizeHelper(node.left, prefix + (isTail ? "    " : "│   "), true, sb);
        }
    }


    public Node(T value) {
        this.value = value;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.colour = true;
    }

    public Node(T value, Node<T> parent, Node<T> left, Node<T> right, boolean colour) {
        this.value = value;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.colour = colour;
    }

    public Node() {
    }
}
