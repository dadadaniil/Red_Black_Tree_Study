package org.example;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        String path = "data/tree_data.txt";
        if (args.length > 0) {
            path = args[0];
        }

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        RedBlackTree tree = new RedBlackTree();
        tree.createTreeFromFile(path);

        System.out.println(tree.visualize());
    }
}