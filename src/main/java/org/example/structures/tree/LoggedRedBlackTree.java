package org.example.structures.tree;

import org.example.utils.database.DatabaseCommunicator;

import static org.example.utils.performance.TimingUtil.measureExecutionTime;

public class LoggedRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T> {

    private DatabaseCommunicator databaseCommunicator;

    @Override
    public void insert(T value) {
        double executionTime = measureExecutionTime("INSERT", () -> super.delete(value));
        databaseCommunicator.logPerformance(1, "INSERT",executionTime);

    }

    @Override
    public void delete(T value) {
        double executionTime = measureExecutionTime("DELETE", () -> super.delete(value));
        databaseCommunicator.logPerformance(1, "DELETE",executionTime);


    }

//    @Override
//    public Optional<Node<T>> search(T value) {
//        return performanceLogger.logActionWithResult("SEARCH", () -> super.search(value));
//    }

}
