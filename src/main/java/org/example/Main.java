package org.example;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        try {
            RedBlackTree<Integer> root = new RedBlackTree<>(10);
            root.insert(70);
            root.insert(6);
            root.insert(8);
            root.insert(11);
            root.insert(22);
            System.out.println(root.visualize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}