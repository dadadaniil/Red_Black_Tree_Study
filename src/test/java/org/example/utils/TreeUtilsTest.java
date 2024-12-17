package org.example.utils;

import org.example.storage.tree.RedBlackTree;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TreeUtilsTest {

//    @Test
//    void testCreateTreeFromFile() throws IOException {
//        String filePath = "testFile.txt";
//        try (FileWriter writer = new FileWriter(filePath)) {
//            writer.write("10\n20\n30\n");
//        }
//
//        RedBlackTree<Integer> tree = TreeUtils.createTreeFromFile(filePath);
//        assertNotNull(tree);
//        assertNotNull(tree.search(10));
//        assertNotNull(tree.search(20));
//        assertNotNull(tree.search(30));
//    }

//    @Test
//    void testCreateTreeFromFileWithInvalidData() throws IOException {
//        String filePath = "testFileInvalid.txt";
//        try (FileWriter writer = new FileWriter(filePath)) {
//            writer.write("10\ninvalid\n30\n");
//        }
//
//        RedBlackTree<Integer> tree = TreeUtils.createTreeFromFile(filePath);
//        assertNotNull(tree);
//        assertNotNull(tree.search(10));
//        assertNull(tree.search(20));
//        assertNotNull(tree.search(30));
//    }

}
