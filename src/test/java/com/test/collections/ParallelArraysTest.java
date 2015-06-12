package com.test.collections;

import com.google.common.collect.Sets;
import com.test.concurrency.ForkJoinTest;
import com.test.generators.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by Matija Vižintin
 * Date: 14. 05. 2015
 * Time: 20.39
 */
public class ParallelArraysTest {

    /**
     * Test shows new parallel operations on arrays.
     */
    @Test
    public void parallelOperations() {
        // crate new empty array and fill it in parallel
        int arraySize = 1000 * 100;
        double[] array = new double[arraySize];
        Arrays.parallelSetAll(array, value -> Math.random());

        // sort array in parallel
        Arrays.parallelSort(array);

        // sum using a stream
        double sum = Arrays.stream(array).sum();

        // parallel prefix - this is actually cum sum
        Arrays.parallelPrefix(array, (left, right) -> left + right);

        // assert
        Assert.assertEquals(sum, array[arraySize - 1], Math.pow(10, -9));
    }

    /**
     * Under the hood parallel stream uses Arrays.parallelSort for sorting the stream. This example should be the same as the parallelSort test in
     * ParallelStreamsTest.
     */
    private Set<String> threads = Sets.newConcurrentHashSet();
    @Test
    public void parallelSort() {
        // sort 10 elements and it should execute in a single thread
        Double[] smallArray = DataGenerator.randomDoubles(10).toArray(new Double[]{});
        threads.clear();
        Arrays.parallelSort(
                smallArray, (o1, o2) -> {
            threads.add(Thread.currentThread().getName());
            return o1.compareTo(o2);
        });
        Assert.assertEquals(1, threads.size());

        // sort 10k elements and it should be executed in parallel
        Double[] bigArray = DataGenerator.randomDoubles(10 * 1000).toArray(new Double[]{});
        threads.clear();
        Arrays.parallelSort(
                bigArray, (o1, o2) -> {
                    threads.add(Thread.currentThread().getName());
                    return o1.compareTo(o2);
                });
        Assert.assertEquals(ForkJoinTest.NO_OF_VCORES, threads.size());
    }
}
