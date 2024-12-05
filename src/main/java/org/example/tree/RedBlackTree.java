package org.example.tree;

import lombok.extern.log4j.Log4j2;
import org.example.node.Node;
import org.example.utils.database.DatabaseHandler;

import java.util.Optional;

import static org.example.utils.performance.TimingUtil.measureExecutionTime;

@Log4j2
public class RedBlackTree<T extends Comparable<T>> extends AbstractBalancedTree<T> {

    public static final boolean RED = false;
    public static final boolean BLACK = true;

    private final DatabaseHandler databaseHandler;
    private final int treeId;

    public RedBlackTree(DatabaseHandler databaseHandler, int treeId) {
        this.databaseHandler = databaseHandler;
        this.treeId = treeId;
    }

    @Override
    public void insert(T value) {
        double duration = measureExecutionTime("insert", () -> {
            Node<T> newNode = new Node<>(value);
            if (root == null) {
                root = newNode;
                root.color = BLACK;
                return;
            }

            Node<T> current = root, parent = null;

            while (current != null) {
                parent = current;

                // Use compareTo for comparisons
                int comparison = value.compareTo(current.value);

                if (comparison < 0) {
                    current = current.left;
                } else if (comparison > 0) {
                    current = current.right;
                } else {
                    return; // Ignore duplicates
                }
            }

            // Attach the new node to its parent
            newNode.parent = parent;
            if (value.compareTo(parent.value) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            // Fix Red-Black Tree properties
            fixInsertion(newNode);
        });
        databaseHandler.logPerformance(treeId, "INSERT", duration);
    }


    @Override
    public void delete(T value) {
        double duration = measureExecutionTime("delete", () -> {
            Optional<Node<T>> optionalNode = search(value);
            if (optionalNode.isEmpty()) return;

            Node<T> nodeToDelete = optionalNode.get();

            Node<T> movedUpNode;
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
        });
        databaseHandler.logPerformance(treeId, "DELETE", duration);
    }

    @Override
    public Optional<Node<T>> search(T value) {
        Node<T> current = root;
        while (current != null && !current.value.equals(value)) {
            if (value.compareTo(current.value) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return Optional.ofNullable(current);
    }

    ;

    void fixInsertion(Node<T> node) {
        while (node != root && node.parent.color == RED) {
            Node<T> parent = node.parent;
            Node<T> grandparent = parent.parent;

            if (parent == grandparent.left) {
                handleLeftSubtreeInsertion(node, parent, grandparent);
            } else {
                handleRightSubtreeInsertion(node, parent, grandparent);
            }
        }
        root.color = BLACK;
    }

    private void handleLeftSubtreeInsertion(Node<T> node, Node<T> parent, Node<T> grandparent) {
        Node<T> uncle = grandparent.right;

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

    private void handleRightSubtreeInsertion(Node<T> node, Node<T> parent, Node<T> grandparent) {
        Node<T> uncle = grandparent.left;

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

    private void recolorAfterInsertion(Node<T> parent, Node<T> uncle, Node<T> grandparent) {
        parent.color = BLACK;
        uncle.color = BLACK;
        grandparent.color = RED;
    }


    public void rotateLeft(Node<T> node) {
        Node<T> rightChild = node.right;
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

    public void rotateRight(Node<T> node) {
        Node<T> leftChild = node.left;
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

    private Node<T> handleSingleChildOrLeafNodeDeletion(Node<T> nodeToDelete, Node<T> child) {
        replaceNode(nodeToDelete, child);
        return child;
    }

    private Node<T> handleTwoChildNodeDeletion(Node<T> nodeToDelete) {
        Node<T> successor = findMinimum(nodeToDelete.right);
        Node<T> movedUpNode = successor.right;

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


    private void fixDeletion(Node<T> node) {
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

    private void handleLeftSiblingCase(Node<T> node) {
        Node<T> sibling = node.parent.right;

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

    private void handleRightSiblingCase(Node<T> node) {
        Node<T> sibling = node.parent.left;

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

    private void recolorAndRotateLeft(Node<T> parent, Node<T> sibling) {
        sibling.color = BLACK;
        parent.color = RED;
        rotateLeft(parent);
    }

    private void recolorAndRotateRight(Node<T> parent, Node<T> sibling) {
        sibling.color = BLACK;
        parent.color = RED;
        rotateRight(parent);
    }

    private void recolorSiblingRed(Node<T> sibling) {
        sibling.color = RED;
    }

    public void recolorParentAndSibling(Node<T> parent, Node<T> sibling) {
        sibling.color = parent.color;
        parent.color = BLACK;
        if (sibling.right != null) {
            sibling.right.color = BLACK;
        }
    }

    public boolean isBlack(Node<T> node) {
        return node == null || node.color == BLACK;
    }

    public boolean isLeftChild(Node<T> node) {
        return node == node.parent.left;
    }


    public Node<T> findMinimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    protected void handleLeftSubtreeViolation(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> grandparent = parent.parent;
        Node<T> uncle = grandparent.right;

        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            uncle.color = BLACK;
            grandparent.color = RED;
        } else {
            if (node == parent.right) {
                rotateLeft(parent);
                node = parent;
            }
            parent.color = BLACK;
            grandparent.color = RED;
            rotateRight(grandparent);
        }
    }

    @Override
    protected void handleRightSubtreeViolation(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> grandparent = parent.parent;
        Node<T> uncle = grandparent.left;

        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            uncle.color = BLACK;
            grandparent.color = RED;
        } else {
            if (node == parent.left) {
                rotateRight(parent);
                node = parent;
            }
            parent.color = BLACK;
            grandparent.color = RED;
            rotateLeft(grandparent);
        }
    }



}
