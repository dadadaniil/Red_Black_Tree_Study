package org.example;


import lombok.extern.log4j.Log4j2;
import org.example.tree.RedBlackTree;
import org.example.utils.TreeUtils;

import static org.example.utils.TreeUtils.createTreeFromFile;

@Log4j2
public class Main {
    public static void main(String[] args) {
        String path = "data/tree_data.txt";
        if (args.length > 0) {
            path = args[0];
        }
        log.info(path);

        RedBlackTree<Integer> intTree = createTreeFromFile(path); // T = Integer
        TreeUtils.printTree(intTree);
    }
}