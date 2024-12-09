package org.example.tree;

import org.example.utils.performance.PerformanceLogger;

public class LoggedRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T> {

    private final PerformanceLogger performanceLogger;

    public LoggedRedBlackTree(PerformanceLogger performanceLogger) {
        this.performanceLogger = performanceLogger;
    }


    @Override
    public void insert(T value) {
        performanceLogger.logAction("INSERT", () -> super.insert(value));
    }

    @Override
    public void delete(T value) {
        performanceLogger.logAction("DELETE", () -> super.delete(value));
    }

//    @Override
//    public Optional<Node<T>> search(T value) {
//        return performanceLogger.logActionWithResult("SEARCH", () -> super.search(value));
//    }

}
