package com.test.streams;

import com.google.common.collect.Sets;
import com.test.ForkJoinTest;
import com.test.generators.DataGenerator;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 14.19
 */
public class ParallelStreamsTest extends LoggingTimedTest {

    /**
     * Simple parallel test. In parallel it filters, maps and prints out elements.
     */
    private int executionCounter = 0;
    @Test
    public void parallel() {
        int noOfStrings = 100;
        List<String> list = DataGenerator.strings(5, noOfStrings);

        // process in parallel and print thread names that execute operations
        list.parallelStream().filter(
                s -> {
                    System.out.format("Filter: input: %s, thread: %s\n", s, Thread.currentThread().getName());
                    return true;
                }).map(
                s -> {
                    System.out.format("Map: input: %s, thread: %s\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                }).forEach(
                s -> {
                    System.out.format("ForEach: input: %s, thread: %s\n", s, Thread.currentThread().getName());
                    executionCounter++;
                });

        // assert
        Assert.assertEquals(noOfStrings, executionCounter);     // since filter does nothing forEach should be executed for every element
    }

    /**
     * This is the parallel sort test, but the sorting actually executes in a single thread. The stream is too short to be executed in parallel.
     *
     * NOTE: starting new thread and parallel execution in costly, so if there is no benefit from it (steam not long enough), sorting is executed in
     * a single thread.
     */
    private Set<String> executingThreads = Sets.newConcurrentHashSet();     // working in parallel
    @Test
    public void parallelSort1() {
        List<String> list = DataGenerator.strings(5, 10);       // length = 10

        // process sorting
        executingThreads.clear();
        list.parallelStream().sorted(
                (o1, o2) -> {
                    System.out.printf("sort: %s <> %s, [%s]\n", o1, o2, Thread.currentThread().getName());
                    executingThreads.add(Thread.currentThread().getName());
                    return o1.compareTo(o2);
                }).collect(Collectors.toList());        // dummy-op, otherwise stream is not executed

        // assert
        Assert.assertEquals(1, executingThreads.size());        // executed in single thread
    }

    /**
     * This is the parallel sort test that is actually executed in parallel in contrast to {@link ParallelStreamsTest#parallelSort1()}. Test (sorting)
     * should be executed on all available threads.
     */
    @Test
    public void parallelSort2() {
        List<String> list = DataGenerator.strings(5, 100 * 1000);       // length = 100k

        // process sorting
        executingThreads.clear();
        list.parallelStream().sorted(
                (o1, o2) -> {
                    // NOTE: printout is disabled due to too much output
                    //System.out.printf("sort: %s <> %s, [%s]\n", o1, o2, Thread.currentThread().getName());
                    executingThreads.add(Thread.currentThread().getName());
                    return o1.compareTo(o2);
                }).collect(Collectors.toList());    // dummy-op, otherwise stream is not executed

        // assert
        Assert.assertEquals(ForkJoinTest.NO_OF_VCORES, executingThreads.size());        // executed on all available threads
    }

    /**
     * This test shows how stream can be created as sequential and then transformed to parallel.ck to sequential.
     *
     * NOTE: don't forget that staring new threads is costly.
     */
    @Test
    public void sequentialToParallel() {
        executingThreads.clear();
        List<Integer> integers = DataGenerator.integers(1000 * 1000)     // 1M of data
                .stream()               // sequential stream
                .filter(
                        integer -> {
                            executingThreads.add(Thread.currentThread().getName());
                            return true;
                        })
                .map(Function.<Integer>identity())
                .parallel()             // transformed in parallel stream
                .collect(Collectors.toList());

        // assert
        Assert.assertEquals(ForkJoinTest.NO_OF_VCORES, executingThreads.size());        // filter was executed on all available threads
    }
}
