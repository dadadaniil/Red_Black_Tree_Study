package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.example.node.Node;
import org.example.tree.RedBlackTree;
import org.example.utils.database.DatabaseHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Log4j2
public class TreeUtils {

    private static final DatabaseHandler databaseHandler= new DatabaseHandler();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T extends Comparable<T>> RedBlackTree<T> createTreeFromFile(String filePath) {
    RedBlackTree<T> tree = new RedBlackTree<>(databaseHandler, 1);

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                br.lines()
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .forEach(line -> insertValueIntoTree(tree, line));

                saveTreeToDatabase(tree, filePath);

    } catch (IOException e) {
        log.error("Error reading file: {}", filePath, e);
    }

    return tree;
}


    private static <T extends Comparable<T>> void insertValueIntoTree(RedBlackTree<T> tree, String line) {
        try {
            T value = parseValue(line);
            tree.insert(value);
        } catch (ClassCastException | NumberFormatException e) {
            log.error("Invalid format in file: {}", line, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> T parseValue(String line) {
        if (line.matches("-?\\d+")) {
            return (T) Integer.valueOf(line);
        } else if (line.matches("-?\\d*\\.\\d+")) {
            return (T) Double.valueOf(line);
        } else {
            return (T) line;
        }
    }

    public static <T extends Comparable<T>> void printTree(RedBlackTree<T> redBlackTree) {
        StringBuilder sb = new StringBuilder();
        visualizeHelper(redBlackTree.root, sb, "", true);
        System.out.println(sb);
    }

    private static <T extends Comparable<T>> void visualizeHelper(Node<T> node, StringBuilder sb, String prefix, boolean isTail) {
        if (node == null) return;

        if (node.right != null) {
            visualizeHelper(node.right, sb, prefix + (isTail ? "|   " : "    "), false);
        }

        sb.append(prefix)
            .append(isTail ? "\\-- " : "/-- ")
            .append(node.value)
            .append(node.color == RedBlackTree.RED ? " (R)" : " (B)")
            .append("\n");

        if (node.left != null) {
            visualizeHelper(node.left, sb, prefix + (isTail ? "    " : "|   "), true);
        }
    }

        private static <T extends Comparable<T>> String serializeTreeToJson(Node<T> node) throws JsonProcessingException {
        if (node == null) return null;

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("value", node.value.toString());
        jsonNode.put("color", node.color == RedBlackTree.RED ? "RED" : "BLACK");

        if (node.left != null) {
            jsonNode.set("left", objectMapper.readTree(serializeTreeToJson(node.left)));
        }

        if (node.right != null) {
            jsonNode.set("right", objectMapper.readTree(serializeTreeToJson(node.right)));
        }

        return jsonNode.toString();
    }

        private static <T extends Comparable<T>> void saveTreeToDatabase(RedBlackTree<T> tree, String filePath) throws JsonProcessingException {
        String treeJson = serializeTreeToJson(tree.root);
        if (treeJson == null) {
            log.error("Tree is empty; cannot save to database.");
            return;
        }

        int fileId = databaseHandler.saveFile(
            filePath.substring(filePath.lastIndexOf('/') + 1),
            filePath,
            "{}"         );

        if (fileId == -1) {
            log.error("Failed to save file metadata.");
            return;
        }

        int treeId = databaseHandler.saveTree(fileId, "Red-Black Tree", 1, treeJson);
        if (treeId == -1) {
            log.error("Failed to save tree: {}", treeJson);
        } else {
            log.info("Tree saved with ID: {}", treeId);
        }
    }
}
