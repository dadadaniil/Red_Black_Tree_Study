package org.example;

public class RedBlackTree<T extends Comparable<T>> {
    public T value;
    public RedBlackTree<T> parent;
    public RedBlackTree<T> left;
    public RedBlackTree<T> right;
    public boolean colour; // true-red, false-black

    public void insert(T key) {
        RedBlackTree<T> node = new RedBlackTree<>(key);
        if (this.value == null) {
            this.value = key;
            this.colour = false;
            return;
        }

        RedBlackTree<T> current = this;
        RedBlackTree<T> parentNode = null;

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
        RedBlackTree<T> node = findNode(key);
        if (node == null) return;

        RedBlackTree<T> y = node;
        RedBlackTree<T> x;
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

    private RedBlackTree<T> findNode(T key) {
        RedBlackTree<T> current = this;
        while (current != null && !key.equals(current.value)) {
            if (key.compareTo(current.value) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return current;
    }

    private void transplant(RedBlackTree<T> u, RedBlackTree<T> v) {
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

    private RedBlackTree<T> minimum(RedBlackTree<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public void leftRotation(RedBlackTree<T> node) {
        RedBlackTree<T> y = node.right;

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

    public void rightRotation(RedBlackTree<T> node) {
        RedBlackTree<T> y = node.left;
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

    public boolean isHealthy(RedBlackTree<T> node) {
        return (redViolationsCheck(node) &&
            blackViolationCheck(node) &&
            blackRootCheck(node));
    }

    public boolean redViolationsCheck(RedBlackTree<T> node) {
        if (!node.colour) {
            return true;
        }
        return node.parent == null || !node.parent.colour;
    }

    public boolean blackViolationCheck(RedBlackTree<T> node) {
        return node == null || calculateBlackHeight(node) != -1;
    }

    private int calculateBlackHeight(RedBlackTree<T> node) {
        if (node == null) {
            return 1;
        }

        int leftHeight = calculateBlackHeight(node.left);
        int rightHeight = calculateBlackHeight(node.right);

        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) {
            return -1;
        }

        return leftHeight + (node.colour ? 0 : 1);
    }


    public boolean blackRootCheck(RedBlackTree<T> node) {
        while (node.parent != null) {
            node = node.parent;
        }
        return !node.colour;
    }

    public void fixDeletionViolation(RedBlackTree<T> node) {
        while (node.parent != null && !node.colour) {
            RedBlackTree<T> parentNode = node.parent;
            RedBlackTree<T> sibling = (node == parentNode.left) ? parentNode.right : parentNode.left;

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

    public void handleRedSibling(RedBlackTree<T> node, RedBlackTree<T> parent, RedBlackTree<T> sibling) {
        sibling.colour = false;
        parent.colour = true;
        if (node == parent.left) {
            leftRotation(parent);
        } else {
            rightRotation(parent);
        }
    }

    public void handleBlackSiblingWithBlackChildren(RedBlackTree<T> node, RedBlackTree<T> sibling) {
        sibling.colour = true;
    }

    public void handleBlackSiblingWithRedChild(RedBlackTree<T> node, RedBlackTree<T> parent, RedBlackTree<T> sibling) {
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

    public boolean isBlackWithBlackChildren(RedBlackTree<T> sibling) {
        return sibling != null &&
            !sibling.colour &&
            (sibling.left == null || !sibling.left.colour) &&
            (sibling.right == null || !sibling.right.colour);
    }

    public void fixInsertionViolation(RedBlackTree<T> node) {
        // no red-red violations, exit
        if (redViolationsCheck(node)) {
            return;
        }

        RedBlackTree<T> parentNode = node.parent;
        RedBlackTree<T> grandparentNode = parentNode.parent;
        if (grandparentNode != null) {
            RedBlackTree<T> uncleNode = (grandparentNode.left == parentNode) ? grandparentNode.right : grandparentNode.left;

            // Case 1: Parent and Uncle are both red (Recoloring Case)
            if (uncleNode != null && uncleNode.colour) {
                handleRedUncleCase(parentNode, grandparentNode, uncleNode);

                // Case 2a: Zig-Zag pattern (Parent and Node are on opposite sides)
            } else if (isZigZag(node, parentNode, grandparentNode)) {
                handleZigZagCase(node, parentNode, grandparentNode);

                // Case 2b: Zig-Zig pattern (Parent and Node are on the same side)
            } else {
                handleZigZigCase(node, parentNode, grandparentNode);
            }
        }
    }

    public void handleRedUncleCase(RedBlackTree<T> parent, RedBlackTree<T> grandparent, RedBlackTree<T> uncle) {
        parent.colour = false;
        uncle.colour = false;
        grandparent.colour = true;
        fixInsertionViolation(grandparent);
    }

    public void handleZigZagCase(RedBlackTree<T> node, RedBlackTree<T> parent, RedBlackTree<T> grandparent) {
        if (node == parent.right) {
            leftRotation(parent);
            node = node.left;
        } else {
            rightRotation(parent);
            node = node.right;
        }
        handleZigZigCase(node, parent, grandparent);
    }

    public void handleZigZigCase(RedBlackTree<T> node, RedBlackTree<T> parent, RedBlackTree<T> grandparent) {
        parent.colour = false;
        grandparent.colour = true;
        if (parent == grandparent.left) {
            rightRotation(grandparent);
        } else {
            leftRotation(grandparent);
        }
    }

    private boolean isZigZag(RedBlackTree<T> node, RedBlackTree<T> parent, RedBlackTree<T> grandparent) {
        return (parent == grandparent.left && node == parent.right) ||
            (parent == grandparent.right && node == parent.left);
    }

    public String visualize() {
        StringBuilder sb = new StringBuilder();
        visualizeHelper(this, sb, "", true);
        return sb.toString();
    }

    private void visualizeHelper(RedBlackTree<T> node, StringBuilder sb, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }
        if (node.right != null) {
            visualizeHelper(node.right, sb, prefix + (isTail ? "│   " : "    "), false);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.value).append(node.colour ? " (R)" : " (B)").append("\n");
        if (node.left != null) {
            visualizeHelper(node.left, sb, prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public RedBlackTree(T value) {
        this.value = value;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.colour = true;
    }

    public RedBlackTree(T value, RedBlackTree<T> parent, RedBlackTree<T> left, RedBlackTree<T> right, boolean colour) {
        this.value = value;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.colour = colour;
    }

    public RedBlackTree() {
    }
}
