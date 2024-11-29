package org.example;


import lombok.extern.log4j.Log4j2;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.example.RedBlackTree.createTreeFromFile;

@Log4j2
public class Main {
    public static void main(String[] args) {
        String path = "data/tree_data.txt";
        if (args.length > 0) {
            path = args[0];
        }
        log.info(path);

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        RedBlackTree<Integer> intTree = createTreeFromFile(path); // T = Integer

        log.info("Using path: {}", path);

        System.out.println(intTree.visualize());
    }
}