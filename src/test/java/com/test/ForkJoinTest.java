package com.test;

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
    public static final int NO_OF_VCORES = 8;          // i7-4980HQ

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
}
