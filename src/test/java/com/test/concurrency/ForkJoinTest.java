package com.test.concurrency;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 13.57
 */
public class ForkJoinTest extends LoggingTimedTest {
    public static final int NO_OF_VCORES = Runtime.getRuntime().availableProcessors();

    /**
     * Simple test of ForkJoinPool
     */
    @Test
    public void poolTest() {
        // initialize a pool and print no of available threads
        ForkJoinPool pool = ForkJoinPool.commonPool();
        System.out.println("No of threads: " + pool.getParallelism());

        Assert.assertEquals(NO_OF_VCORES - 1, pool.getParallelism());            // expected: no of (virtual) cores - 1
    }

    /**
     * This method returns the same result as the upper one, except here the result is read from a variable and not
     * from the pool instance.
     */
    @Test
    public void getParallelism() {
        int parallelism = ForkJoinPool.getCommonPoolParallelism();
        System.out.println("Common parallelism: " + parallelism);

        Assert.assertEquals(NO_OF_VCORES - 1, parallelism);
    }
}
