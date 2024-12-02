package org.example.utils;

import lombok.extern.log4j.Log4j2;
import org.example.node.Node;
import org.example.tree.RedBlackTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@Log4j2
public class TreeUtils {
    public static <T extends Comparable<T>> RedBlackTree<T> createTreeFromFile(String filePath) {
        RedBlackTree<T> tree = new RedBlackTree<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        @SuppressWarnings("unchecked")
                        T value = (T) parseValue(line);
                        tree.insert(value);
                    } catch (ClassCastException | NumberFormatException e) {
                        log.error("Invalid format in file: {}", line);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath);
            e.printStackTrace();
        }
        return tree;
    }

    private static Object parseValue(String line) {
        if (line.matches("-?\\d+")) {
            return Integer.parseInt(line); // Parse as Integer
        } else if (line.matches("-?\\d*\\.\\d+")) {
            return Double.parseDouble(line); // Parse as Double
        } else {
            return line;
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


}
