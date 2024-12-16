package org.example.storage.tree;


import static org.example.utils.performance.TimingUtil.measureExecutionTime;

public class LoggedRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T> {

    @Override
    public void insert(T value) {
        measureExecutionTime("INSERT", () -> super.insert(value));
    }

    @Override
    public void delete(T value) {
        measureExecutionTime("DELETE", () -> super.delete(value));
    }

}
