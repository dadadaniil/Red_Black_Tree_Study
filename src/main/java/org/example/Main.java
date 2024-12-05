package org.example;


import lombok.extern.log4j.Log4j2;
import org.example.tree.RedBlackTree;
import org.example.utils.TreeUtils;
import org.example.utils.database.AbstractDatabaseHandler;
import org.example.utils.database.DatabaseHandlerFactory;

import static org.example.utils.TreeUtils.createTreeFromFile;

@Log4j2
public class Main {
    public static void main(String[] args) {
        String path = "data/tree_data.txt";
        if (args.length > 0) {
            path = args[0];
        }
        log.info("Creating tree from file by path: {}", path);

        RedBlackTree<Integer> intTree = createTreeFromFile(path);
        TreeUtils.printTree(intTree);
    }
}